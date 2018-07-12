package module.scenarios.format

import com.pharbers.ErrorCode
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

trait ParsingTrait {
	def ps2m(input: Option[String Map JsValue])
	        (out: String Map JsValue => (Option[String Map JsValue], Option[JsValue])): (Option[String Map JsValue], Option[JsValue]) = {
		input match {
			case None => (None, None)
			case Some(o) => out(o)
		}
	}
	
	val formatHospitals: String Map JsValue => (Option[String Map JsValue], Option[JsValue])  = { m =>
		try {
			val data = m("scenario")
			val reVal = (data \ "current" \ "dest_goods").as[List[String Map JsValue]].groupBy(g => g("dest_id").as[String]).map { dg =>
				
				val hospital = (data \ "current" \ "connect_dest").as[List[String Map JsValue]].find(f => f("id").as[String] == dg._1).map ( x =>
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
					(data \ "current" \ "connect_goods").as[List[String Map JsValue]].find(f => g("goods_id").as[String] == f("id").as[String]).map ( x =>
						Map("id" -> x("id"),
							"name" -> x("category"),
							"share" -> (g("relationship") \ "share").as[JsValue],
							"previoussales" -> (g("relationship") \ "pre_sales").as[JsValue],
							"marketpotential" -> (g("relationship") \ "potential").as[JsValue],
							"contributionrate" -> (g("relationship") \ "cont_rate").as[JsValue])
					
					).getOrElse(throw new Exception("is null"))
				}
				
				val representative = (data \ "current" \ "dest_reso").as[List[String Map JsValue]].filter(f => dg._1 == f("dest_id").as[String]).flatMap ( x =>
					(data \ "current" \ "connect_reso").as[List[String Map JsValue]].filter(ff => x("reso_id").as[String] == ff("id").as[String]).map( r =>
						Map("name" -> r("rep_name"), "avatar" -> r("avatar"))
					)
				)
				
				hospital ++ Map("medicines" -> toJson(goods)) ++ Map("representives" -> toJson(representative))
				
			}.toList
			
			(Some(Map("result" -> toJson(
				Map("currentMonth" -> toJson((data \ "current" \ "name").as[String]),
					"hospitals" -> toJson(reVal)
				))
			)), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
		
		
	}
	
	val formatBudget: String Map JsValue => (Option[String Map JsValue], Option[JsValue]) = { m =>
		try {
			val data = m("scenario")
			val total = (data \ "current" \ "connect_reso").as[List[String Map JsValue]].
				find(f => f("type").as[String] == "money").map( x =>
				Map("total" -> (x("relationship") \ "total_budget_money").as[JsValue])
			).getOrElse(throw new Exception("is null"))
			val used = (data \ "current" \ "dest_goods_reso").as[List[String Map JsValue]].
				map(x => (x("relationship") \ "user_budget_money").as[Double]).sum
			(Some(Map("result" -> toJson(total ++ Map("used" -> toJson(used))))), None)
		} catch{
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	val formatHumans: String Map JsValue => (Option[String Map JsValue], Option[JsValue]) = { m =>
		try {
			val data = m("scenario")
			val total = (data \ "current" \ "connect_reso").as[List[String Map JsValue]].
					find(f => f("type").as[String] == "day").map(x => (x("relationship") \ "total_budget_day").as[Int]).
					getOrElse(throw new Exception("is null"))
			
			val reVal = (data \ "current" \ "dest_goods_reso").as[List[String Map JsValue]].groupBy(g => g("reso_id").as[String]).flatMap { x =>
				(data \ "current" \ "connect_reso").as[List[String Map JsValue]].find(f => f("id").as[String] == x._1).map { y =>
					val used = x._2.map(z => (z("relationship") \ "user_budget_day").as[Double]).sum
					Map("name" -> y("rep_name"),
						"total" -> toJson(total),
						"used" -> toJson(used))
				}
			}.toList
			(Some(Map("result" -> toJson(reVal))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	val formatHospitalDetails: String Map JsValue => (Option[String Map JsValue], Option[JsValue]) = { m =>
		try {
			val data = m("scenario")
			val hospital_id = (m("data") \ "condition" \ "hospital_id").as[String]
			val hospital = (data \ "current" \ "connect_dest").as[List[String Map JsValue]].find(f => f("id").as[String] == hospital_id).
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
					"news" -> Map.empty,
					"policy" -> Map.empty
				))

			val medicines = (data \ "current" \ "dest_goods").as[List[String Map JsValue]].filter(f => f("dest_id").as[String] == hospital_id).
				map { x =>
					(data \ "current" \ "connect_goods").as[List[String Map JsValue]].
						find(f => f("id").as[String] == x("goods_id").as[String]).
						map { basic =>
							Map("id" -> basic("id"),
								"name" -> basic("category"),
								"marketpotential" -> toJson(12134523),
								"potentialgrowth" -> x("potential"),
								"previoussales" -> x("pre_sales"),
								"previousgrowth" -> toJson(12134523),
								"share" -> x("share"),
								"contributionrate" -> x("cont_rate"),
								"detail" -> toJson(
									Map("id" -> toJson(s"${basic("id").as[String]}_detail"),
//										"value"
//										"type" -> basic("category")
									))
							)
						}
				}

			(Some(Map("" -> toJson(1))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
}
