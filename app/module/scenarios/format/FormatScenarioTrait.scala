package module.scenarios.format

import com.mongodb.casbah.Imports._
import com.pharbers.ErrorCode
import com.pharbers.bmmessages.CommonModules
import com.pharbers.cliTraits.DBTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
import play.api.libs.json._
import play.api.libs.json.Json.toJson
import module.common.datamodel.cdr

trait FormatScenarioTrait extends cdr {
	def format(input: Option[String Map JsValue])
	        (out: (String Map JsValue, CommonModules) => (Option[String Map JsValue], Option[JsValue]))
			(implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		input match {
			case None => (None, None)
			case Some(o) => out(o, cm)
		}
	}

	def queryReport(rid: String)
	               (out: DBObject => Map[String, JsValue])
	               (implicit cm: CommonModules): String Map JsValue = {

		val conn: dbInstanceManager = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
		val db: DBTrait = conn.queryDBInstance("report").get
		db.queryObject(DBObject("_id" -> new ObjectId(rid)), "reports")(out).get
	}
	
	val jvdr: JsValue => String Map JsValue = { jv =>
		jv.as[String Map JsValue].map { m =>
		    	m._2 match {
				case str: JsString => m._1 -> toJson(str)
				case num: JsNumber => m._1 -> toJson(num)
				case bool: JsBoolean => m._1 -> toJson(bool)
				case obj: JsObject => m._1 -> toJson(jvdr(obj.as[JsValue]))
				case array: JsArray => m._1 -> toJson(array.value.toList.map(x => jvdr(x)))
			}
		}
	}
	
	val formatHospitals: (String Map JsValue, CommonModules) => (Option[String Map JsValue], Option[JsValue])  = { (m, c) =>
		
		val data = jvdr(jvdr(m("scenario"))("current"))
		val reVal = data("dest_goods").as[List[String Map JsValue]].groupBy(g => g("dest_id").as[String]).map { dg =>
			
			val hospital = data("connect_dest").as[List[String Map JsValue]].find(f => f("id").as[String] == dg._1).map ( x =>
				Map("hospid" -> x("id"),
					"name" -> x("hosp_name"),
					"category" -> x("category"),
					"hosp_level" -> x("hosp_level"),
					"department" -> x("features_outpatient"),
					"beds" -> x("beds"),
					"outpatient" -> x("outpatient_year"),
					"surgery" -> x("surgery_year")
				)
			).getOrElse(throw new Exception("is null"))
			
			val goods = dg._2.map { g =>
				data("connect_goods").as[List[String Map JsValue]].find(f => g("goods_id").as[String] == f("id").as[String]).map ( x =>
					Map("id" -> x("id"),
						"name" -> x("category"),
						"share" -> (g("relationship") \ "share").as[JsValue],
						"previoussales" -> (g("relationship") \ "pre_sales").as[JsValue],
						"marketpotential" -> (g("relationship") \ "potential").as[JsValue],
						"contributionrate" -> (g("relationship") \ "cont_rate").as[JsValue])
				
				).getOrElse(throw new Exception("is null"))
			}
			
			val representative = data("dest_reso").as[List[String Map JsValue]].filter(f => dg._1 == f("dest_id").as[String]).flatMap ( x =>
				data("connect_reso").as[List[String Map JsValue]].filter(ff => x("reso_id").as[String] == ff("id").as[String]).map( r =>
					Map("name" -> r("rep_name"), "avatar" -> r("avatar"))
				)
			)
			
			hospital ++ Map("medicines" -> toJson(goods)) ++ Map("representives" -> toJson(representative))
			
		}.toList
		
		(Some(Map("result" -> toJson(
			Map("currentMonth" -> toJson(data("name").as[String]),
				"hospitals" -> toJson(reVal)
			))
		)), None)
	}
	
	val formatBudget: (String Map JsValue, CommonModules)=> (Option[String Map JsValue], Option[JsValue]) = { (m, c) =>
		val data = jvdr(jvdr(m("scenario"))("current"))
		val total = data("connect_reso").as[List[String Map JsValue]].find(f => f("type").as[String] == "money").
			map( x => Map("total" -> (x("relationship") \ "total_budget_money").as[JsValue])).
			getOrElse(throw new Exception("is null"))
		val used = data("dest_goods_reso").as[List[String Map JsValue]].
			map(x => (x("relationship") \ "user_budget_money").as[Double]).sum
		(Some(Map("result" -> toJson(total ++ Map("used" -> toJson(used))))), None)
	}
	
	val formatHumans: (String Map JsValue, CommonModules) => (Option[String Map JsValue], Option[JsValue]) = { (m, c) =>
		val data = jvdr(jvdr(m("scenario"))("current"))
		val total = data("connect_reso").as[List[String Map JsValue]].
				find(f => f("type").as[String] == "day").map(x => (x("relationship") \ "total_budget_day").as[Int]).
				getOrElse(throw new Exception("is null"))
		
		val reVal = data("dest_goods_reso").as[List[String Map JsValue]].groupBy(g => g("reso_id").as[String]).flatMap { x =>
			data("connect_reso").as[List[String Map JsValue]].find(f => f("id").as[String] == x._1).map { y =>
				val used = x._2.map(z => (z("relationship") \ "user_budget_day").as[Double]).sum
				Map("name" -> y("rep_name"),
					"total" -> toJson(total),
					"used" -> toJson(used))
			}
		}.toList
		(Some(Map("result" -> toJson(reVal))), None)
	}

	val formatHospitalDetails: (String Map JsValue, CommonModules) => (Option[String Map JsValue], Option[JsValue]) = { (m, c) =>
		try {
			implicit val cm: CommonModules = c

			val data = jvdr(jvdr(m("scenario"))("current"))
			val hospital_id = (m("data") \ "condition" \ "hospital_id").as[String]
			val connect_goods = data("connect_goods").as[List[String Map JsValue]]
			
			val hospital = data("connect_dest").as[List[String Map JsValue]].find(f => f("id").as[String] == hospital_id).
				map ( x => Map("id" -> toJson(hospital_id),
					"name" -> x("hosp_name"),
					"basicinfo" -> toJson(
						Map("type" -> x("category"),
							"hosp_level" -> x("hosp_level"),
							"department" -> x("features_outpatient"),
							"beds" -> x("beds"),
							"outpatient" -> x("outpatient_year"),
							"surgery" -> x("surgery_year"),
							"hospitalizations" -> x("stationierung_year")
						)),
					"news" -> Json.parse("{}"),
					"policy" -> Json.parse("{}")
				)).getOrElse(throw new Exception("is null"))

			val goods = data("dest_goods").as[List[String Map JsValue]].
				filter( f => f("dest_id").as[String] == hospital_id).
				map ( x => connect_goods.find(f => f("id").as[String] == x("goods_id").as[String]).map ( _ ++ x).
				getOrElse(throw new Exception("is null"))).groupBy(g => g("category").as[String]).map { x =>
				val head = x._2.head
				
				val details = x._2.map ( d =>
					Map("product_name" -> d("med_name"),
						"type" -> toJson(x._1),
						"treatmentarea" -> d("therapeutic_field"),
						"selltime" -> d("set_time"),
						"medicalinsurance" -> d("insure_type"),
						"development" -> d("research_type"),
						"companyprice" -> d("ref_price")
					)
				) ++ (head("relationship") \ "compete_goods").as[List[String Map JsValue]].flatMap { cg =>
					connect_goods.filter(f => f("id").as[String] == cg("goods_id").as[String]).map { d =>
						Map("product_name" -> d("med_name"),
							"type" -> toJson(x._1),
							"treatmentarea" -> d("therapeutic_field"),
							"selltime" -> d("set_time"),
							"medicalinsurance" -> d("insure_type"),
							"development" -> d("research_type"),
							"companyprice" -> d("ref_price")
						)
					}
				}
				val competitionproducts = (head("relationship") \ "compete_goods").as[List[String Map JsValue]].flatMap { x =>
					connect_goods.filter(f => f("id").as[String] == x("goods_id").as[String])
				}

				val history = jvdr(m("scenario"))("post").as[List[String Map JsValue]].flatMap { p =>
//					val reVal = queryReport(p("report_id").as[String])(dr)

					p("dest_goods_reso").as[List[String Map JsValue]].map { x =>
						p("connect_reso").as[List[String Map JsValue]].find(f => f("id").as[String] == x("reso_id").as[String]).
							map( d =>
								Map("time" -> toJson(s"周期${p("phase").as[Int]}"),
									"representative" -> d("rep_name"),
									"timemanagement" -> (x("relationship") \ "user_budget_day").as[JsValue],
									"budgetallocation" -> (x("relationship") \ "user_budget_money").as[JsValue],
									"budgetratio" -> toJson(1),
									"indicator" -> (x("relationship") \ "user_indicators").as[JsValue],
									"growth" -> toJson(1),
									"achievementrate" -> toJson(1)
								)
							).getOrElse(throw new Exception("is null"))
					}
				}


				Map("id" -> head("id"),
					"name" -> toJson(x._1),
					"marketpotential" -> (head("relationship") \ "potential").as[JsValue],
					"potentialgrowth" -> toJson(12134523),
					"previoussales" -> (head("relationship") \ "pre_sales").as[JsValue],
					"previousgrowth" -> toJson(12134523),
					"share" -> (head("relationship") \ "share").as[JsValue],
					"contributionrate" -> (head("relationship") \ "cont_rate").as[JsValue],
					"detail" -> toJson(Map(
							"id" -> toJson(s"${head("id").as[String]}_detail"),
							"value" -> toJson(details)
						)),
					"history" -> toJson(Map(
						"id" -> toJson(s"${head("id").as[String]}_history"),
						"value" -> toJson(history)
					)),
					"competitionproducts" -> toJson(Map(
						"id" -> toJson(s"${head("id").as[String]}_competitionproducts"),
						"value" -> toJson(competitionproducts)
					))
				)
			}.toList

			
			
			(Some(Map("result" -> toJson(
				Map("hospital" -> toJson(hospital),
					"medicines" -> toJson(goods))
			))), None)
		} catch {
			case ex: Exception =>
				println(ex.getMessage)
				(None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
}
