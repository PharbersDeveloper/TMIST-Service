package module.scenarios.format

import com.pharbers.ErrorCode
import play.api.libs.json.{JsValue, Json}
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
						"department" -> x("department"),
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
	
//	val formatHospitalDetails: String Map JsValue => (Option[String Map JsValue], Option[JsValue]) = { m =>
//		try {
//			val data = m("scenario")
//
//		} catch {
//			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
//		}
//	}


	def getHospDetail(data: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {

		val hospDetail =
			"""
			  |{
			  |"type": "humans_progress",
			  |"attribute":
			  |{
			  |    "hospital": {
			  |        "id": "idididi",
			  |        "name": "中日医院",
			  |        "basicinfo": {
			  |            "type": "综合",
			  |            "level": "综合/三甲",
			  |            "department": "皮肤科",
			  |            "beds": "1000",
			  |            "outpatient": 1234545,
			  |            "surgery": 1000,
			  |            "hospitalizations": 1000
			  |        },
			  |        "news": { },
			  |        "policy": { }
			  |    },
			  |    "medicines": [
			  |        {
			  |            "id": "medicine000",
			  |            "name": "口服抗生素",
			  |            "marketpotential": 333444555,
			  |            "potentialgrowth": 99,
			  |            "previoussales": 555444,
			  |            "previousgrowth": 99,
			  |            "share": 12,
			  |            "contributionrate": 4,
			  |            "detail": {
			  |                "id": "medicine000_detail",
			  |                "value": [
			  |                    {
			  |                        "id": "霉素",
			  |                        "type": "口服抗生素",
			  |                        "treatmentarea": "抗生素",
			  |                        "selltime": "2000",
			  |                        "medicalinsurance": "甲类",
			  |                        "development": "首仿",
			  |                        "companyprice": 44
			  |                    }
			  |                ]
			  |            },
			  |            "history": {
			  |                "id": "medicine000_history",
			  |                "value": [
			  |                    {
			  |                        "time": "星期一",
			  |                        "representative": "小三",
			  |                        "timemanagement": "12天",
			  |                        "budgetallocation": 5000,
			  |                        "budgetratio": 5000,
			  |                        "indicator": 50000,
			  |                        "growth": 1.23,
			  |                        "achievementrate": 95
			  |                    }
			  |                ]
			  |            },
			  |            "competitionproducts": {
			  |                "id": "medicine000_competitionproducts",
			  |                "value": [ ]
			  |            }
			  |        },{
			  |            "id": "medicine001",
			  |            "name": "口服护发素",
			  |            "marketpotential": 333444555,
			  |            "potentialgrowth": 99,
			  |            "previoussales": 555444,
			  |            "previousgrowth": 99,
			  |            "share": 12,
			  |            "contributionrate": 4,
			  |            "detail": {
			  |                "id": "medicine001_detail",
			  |                "value": [
			  |                    {
			  |                        "id": "霉素",
			  |                        "type": "口服抗生素",
			  |                        "treatmentarea": "抗生素",
			  |                        "selltime": "2000",
			  |                        "medicalinsurance": "甲类",
			  |                        "development": "首仿",
			  |                        "companyprice": 44
			  |                    }
			  |                ]
			  |            },
			  |            "history": {
			  |                "id": "medicine001_history",
			  |                "value": [
			  |                    {
			  |                        "time": "星期一",
			  |                        "representative": "小三",
			  |                        "timemanagement": "12天",
			  |                        "budgetallocation": 5000,
			  |                        "budgetratio": 5000,
			  |                        "indicator": 50000,
			  |                        "growth": 1.23,
			  |                        "achievementrate": 95
			  |                    }
			  |                ]
			  |            },
			  |            "competitionproducts": {
			  |                "id": "medicine001_competitionproducts",
			  |                "value": [ ]
			  |            }
			  |        },{
			  |            "id": "medicine002",
			  |            "name": "口服叶绿素",
			  |            "marketpotential": 333444555,
			  |            "potentialgrowth": 99,
			  |            "previoussales": 555444,
			  |            "previousgrowth": 99,
			  |            "share": 12,
			  |            "contributionrate": 4,
			  |            "detail": {
			  |                "id": "medicine002_detail",
			  |                "value": [
			  |                    {
			  |                        "id": "霉素",
			  |                        "type": "口服抗生素",
			  |                        "treatmentarea": "抗生素",
			  |                        "selltime": "2000",
			  |                        "medicalinsurance": "甲类",
			  |                        "development": "首仿",
			  |                        "companyprice": 44
			  |                    }
			  |                ]
			  |            },
			  |            "history": {
			  |                "id": "medicine002_history",
			  |                "value": [
			  |                    {
			  |                        "time": "星期一",
			  |                        "representative": "小三",
			  |                        "timemanagement": "12天",
			  |                        "budgetallocation": 5000,
			  |                        "budgetratio": 5000,
			  |                        "indicator": 50000,
			  |                        "growth": 1.23,
			  |                        "achievementrate": 95
			  |                    }
			  |                ]
			  |            },
			  |            "competitionproducts": {
			  |                "id": "medicine002_competitionproducts",
			  |                "value": [ ]
			  |            }
			  |        }
			  |    ]
			  |}
			  |}
			""".stripMargin

		val version =
			"""
			  |{
			  |     "major": 1,
			  |     "minor": 0
			  |}
			""".stripMargin

		(Some(Map(
			"timestamp" -> toJson(1530689119000L),
			"version" -> Json.parse(version),
			"data" -> Json.parse(hospDetail)
		)), None)
	}
}
