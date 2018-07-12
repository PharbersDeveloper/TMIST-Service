package module.scenarios.format

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
	
	def filterZip(data: JsValue)(out: JsValue => String Map JsValue): String Map JsValue = out(data)
	
	val zfcDestGoods: JsValue => String Map JsValue = { data =>
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
		Map("result" -> toJson(
				Map("currentMonth" -> toJson((data \ "current" \ "name").as[String]),
					"hospitals" -> toJson(reVal)
				))
		)
	}
}
