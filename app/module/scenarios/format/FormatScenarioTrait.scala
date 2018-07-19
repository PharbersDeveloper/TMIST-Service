package module.scenarios.format


import com.mongodb.casbah.Imports._
import play.api.libs.json._
import play.api.libs.json.Json.toJson
import module.common.datamodel.cdr

trait FormatScenarioTrait extends cdr {
	def format(input: Option[String Map JsValue])
	          (out: String Map JsValue => (Option[String Map JsValue], Option[JsValue])): (Option[String Map JsValue], Option[JsValue]) = {
		input match {
			case None => (None, None)
			case Some(o) => out(o)
		}
	}
	
	val jvdr: JsValue => String Map JsValue = { jv =>
		jv.as[String Map JsValue].map { m =>
			m._2 match {
				case str: JsString => m._1 -> toJson(str)
				case num: JsNumber => m._1 -> toJson(num)
				case bool: JsBoolean => m._1 -> toJson(bool)
				case obj: JsObject => m._1 -> toJson(jvdr(obj.as[JsValue]))
				case array: JsArray => m._1 -> toJson(array.value.toList.map(x => jvdr(x)))
				case _ => ???
			}
		}
	}
	
	def searchJv(jv: JsValue)(key: String): String Map JsValue = {
		val mpjv = jv.as[JsObject].value.toMap
		if (mpjv.contains(key)) {Map(key -> mpjv(key))} else {
			mpjv.filter(f => f._2.isInstanceOf[JsObject] || f._2.isInstanceOf[JsArray]).flatMap { x =>
				x._2 match {
					case obj: JsObject => searchJv(obj.as[JsValue])(key)
					case array: JsArray => array.value.toList.map(searchJv(_)(key)).head
					case _ => ???
				}
			}
		}
	}
	
	val formatHospitals: String Map JsValue => (Option[String Map JsValue], Option[JsValue]) = { m =>
		val data = jvdr(jvdr(m("scenario"))("current"))
		val phase = data("phase").as[Int]
		val dest_goods = jvdr(m("scenario"))("past").as[List[String Map JsValue]].
			find(f => f("phase").as[Int] == phase - 1).get("dest_goods").as[List[String Map JsValue]]
		
		val reVal = data("dest_goods").as[List[String Map JsValue]].groupBy(g => g("dest_id").as[String]).map { dg =>
			
			val hospital = data("connect_dest").as[List[String Map JsValue]].find(f => f("id").as[String] == dg._1).map(x =>
				Map("hospid" -> x("id"),
					"name" -> x("hosp_name"),
					"category" -> x("hosp_category"),
					"hosp_level" -> x("hosp_level"),
					"department" -> x("focus_department"),
					"beds" -> x("beds"),
					"outpatient" -> x("outpatient_yearly"),
					"surgery" -> x("surgery_yearly")
				)
			).getOrElse(throw new Exception("is null"))

			val goods = dg._2.map { g =>
				val sales = dest_goods.find(f => f("goods_id").as[String] == g("goods_id").as[String] &&
					f("dest_id").as[String] == g("dest_id").as[String]).
					map(x => toJson((x("relationship") \ "sales").as[Double])).getOrElse(toJson(0))
				val share = dest_goods.find(f => f("goods_id").as[String] == g("goods_id").as[String] &&
					f("dest_id").as[String] == g("dest_id").as[String]).
					map(x => toJson((x("relationship") \ "share").as[Double])).getOrElse(toJson(0))
				data("connect_goods").as[List[String Map JsValue]].find(f => g("goods_id").as[String] == f("id").as[String]).map { x =>
					
					Map("id" -> x("id"),
						"name" -> x("prod_category"),
						"share" -> share,
						"previoussales" -> sales,
						"potential" -> (g("relationship") \ "potential").as[JsValue],
						"contributionrate" -> (g("relationship") \ "contri_rate").as[JsValue])
					
				}.getOrElse(throw new Exception("is null"))
			}
			
			val representative = data("dest_rep").as[List[String Map JsValue]].filter(f => dg._1 == f("dest_id").as[String]).flatMap(x =>
				data("connect_rep").as[List[String Map JsValue]].filter(ff => x("rep_id").as[String] == ff("id").as[String]).map(r =>
					Map("name" -> r("rep_name"), "avatar" -> r("rep_image"))
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
	
	val formatBudget: String Map JsValue => (Option[String Map JsValue], Option[JsValue]) = { m =>
		val data = jvdr(jvdr(m("scenario"))("current"))
		val total = data("connect_reso").as[List[String Map JsValue]].find(f => f("type").as[String] == "money").
			map(x => Map("total" -> (x("relationship") \ "value").as[JsValue])).
			getOrElse(throw new Exception("is null"))
		val used = data("dest_goods_rep").as[List[String Map JsValue]].
			map(x => (x("relationship") \ "user_input_money").as[Double]).sum
		(Some(Map("result" -> toJson(total ++ Map("used" -> toJson(used))))), None)
	}
	
	val formatHumans: String Map JsValue => (Option[String Map JsValue], Option[JsValue]) = { m =>
		val data = jvdr(jvdr(m("scenario"))("current"))
		val total = data("connect_reso").as[List[String Map JsValue]].
			find(f => f("type").as[String] == "day").map(x => (x("relationship") \ "value").as[Int]).
			getOrElse(throw new Exception("is null"))
		
		val reVal = data("dest_goods_rep").as[List[String Map JsValue]].groupBy(g => g("rep_id").as[String]).flatMap { x =>
			data("connect_rep").as[List[String Map JsValue]].find(f => f("id").as[String] == x._1).map { y =>
				val used = x._2.map(z => (z("relationship") \ "user_input_day").as[Double]).sum
				Map("name" -> y("rep_name"),
					"total" -> toJson(total),
					"used" -> toJson(used))
			}
		}.toList
		(Some(Map("result" -> toJson(reVal))), None)
	}
	
	val formatHospitalDetails: String Map JsValue => (Option[String Map JsValue], Option[JsValue]) = { m =>
		val hospital_id = searchJv(m("data"))("hospital_id")("hospital_id").as[String]
		val current = searchJv(m("scenario"))("current")("current")
		val past = searchJv(m("scenario"))("past")("past").as[List[String Map JsValue]]
		val connect_goods = searchJv(current)("connect_goods")("connect_goods").as[List[String Map JsValue]]
		val phase = searchJv(current)("phase")("phase").as[Int]
		
		val hospital = searchJv(current)("connect_dest")("connect_dest").as[List[String Map JsValue]].
			find(f => f("id").as[String] == hospital_id).map(x =>
			Map("id" -> toJson(hospital_id),
				"name" -> x("hosp_name"),
				"basicinfo" -> toJson(
					Map("type" -> x("hosp_category"),
						"hosp_level" -> x("hosp_level"),
						"department" -> x("focus_department"),
						"beds" -> x("beds"),
						"outpatient" -> x("outpatient_yearly"),
						"surgery" -> x("surgery_yearly"),
						"hospitalizations" -> x("inpatient_yearly"))),
				"news" -> Json.parse("{}"),
				"policy" -> Json.parse("{}")
			)).getOrElse(throw new Exception("is null"))
		
		val goods = searchJv(current)("dest_goods")("dest_goods").
			as[List[String Map JsValue]].filter(f => f("dest_id").as[String] == hospital_id).
			map(x => connect_goods.find(f => f("id").as[String] == x("goods_id").as[String]).map(_ ++ x).
				getOrElse(throw new Exception("is null"))).map { details =>
			
			val basicInfo = ((details("relationship") \ "compete_goods").as[List[String Map JsValue]].
				map(x => x("goods_id").as[String]) :+ details("goods_id").as[String]).map { x =>
				connect_goods.find(f => f("id").as[String] == x).map { d =>
					Map("product_name" -> d("prod_name"),
						"type" -> details("prod_category"),
						"treatmentarea" -> d("therapeutic_field"),
						"selltime" -> d("launch_time"),
						"medicalinsurance" -> d("insure_type"),
						"development" -> d("research_type"),
						"companyprice" -> d("ref_price")
					)
				}.getOrElse(throw new Exception(""))
			}
			
			val p = past.find(f => f("phase").as[Int] == phase - 1).map(x => toJson(x)).getOrElse(throw new Exception("is null"))
			
			val mmp = searchJv(p)("dest_goods")("dest_goods").
				as[List[String Map JsValue]].find(f => f("dest_id").as[String] == hospital_id && f("goods_id").as[String] == details("goods_id").as[String]).get
			
			val history = past.flatMap { p =>
				p("dest_goods_rep").as[List[String Map JsValue]].
					filter(f => f("dest_id").as[String] == hospital_id && f("goods_id").as[String] == details("goods_id").as[String]).map { x =>
					p("connect_rep").as[List[String Map JsValue]].find(f => f("id").as[String] == x("rep_id").as[String]).map { d =>
						Map("time" -> toJson(s"周期${p("phase").as[Int]}"),
							"representative" -> d("rep_name"),
							"timemanagement" -> (x("relationship") \ "user_input_day").as[JsValue],
							"budgetallocation" -> (x("relationship") \ "user_input_money").as[JsValue],
							"budgetratio" -> (x("relationship") \ "budget_proportion").as[JsValue],
							"indicator" -> (x("relationship") \ "user_input_target").as[JsValue],
							"growth" -> (x("relationship") \ "target_growth").as[JsValue],
							"achievementrate" -> (x("relationship") \ "achieve_rate").as[JsValue]
						)
					}
				}
			}
			
			val overview = Map("key" -> toJson("药品市场潜力"), "value" -> (details("relationship") \ "potential").as[JsValue]) ::
				Map("key" -> toJson("增长潜力"), "value" -> (details("relationship") \ "potential_growth").as[JsValue]) ::
				Map("key" -> toJson("上期销售额"), "value" -> (mmp("relationship") \ "sales").as[JsValue]) ::
				Map("key" -> toJson("上期增长"), "value" -> (mmp("relationship") \ "sales_growth").as[JsValue]) ::
				Map("key" -> toJson("份额"), "value" -> (mmp("relationship") \ "share").as[JsValue]) ::
				Map("key" -> toJson("上期贡献率"), "value" -> (details("relationship") \ "contri_rate").as[JsValue]) :: Nil
			
			Map("id" -> details("id"),
				"name" -> toJson(details("prod_category")),
				"overview" -> toJson(overview),
				"detail" -> toJson(Map(
					"id" -> toJson(s"${details("id").as[String]}_detail"),
					"value" -> toJson(basicInfo))),
				"history" -> toJson(Map(
					"id" -> toJson(s"${details("id").as[String]}_history"),
					"value" -> toJson(history))),
				"competitionproducts" -> toJson(Map(
					"id" -> toJson(s"${details("id").as[String]}_competitionproducts"),
					"value" -> Json.parse("[]")
				))
			)
		}
		
		
		(Some(Map("result" -> toJson(
			Map("hospital" -> toJson(hospital),
				"medicines" -> toJson(goods))
		))), None)
	}
}