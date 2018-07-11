package module.scenarios.format

import play.api.libs.json.JsValue
import play.api.libs.json.Json._
import module.scenarios.ProposalMessage._
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.bmmessages.{CommonModules, MessageDefines}

object ScenarioFormatModule extends ModuleTrait with parsing {

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])
                   (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_formatQueryHospLst(data) =>
            (pr, None)
//			ps2m(pr)(formatHosptial)
        case _ => ???
    }



}

trait parsing {
	def ps2m(input: Option[String Map JsValue])
			(out: String Map JsValue => (Option[String Map JsValue], Option[JsValue])): (Option[String Map JsValue], Option[JsValue]) = {
		input match {
			case None => (None, None)
			case Some(o) => out(o)
		}
	}

	def filterZip(data: JsValue)(out: JsValue => List[String Map JsValue]): List[String Map JsValue] = out(data)


	val zfcDestGoods: JsValue => List[String Map JsValue] = { data =>
		val a = (data \ "current" \ "dest_goods").as[List[String Map JsValue]].groupBy(g => g("dest_id").as[String]).map { dg =>

			val hospital = (data \ "current" \ "connect_dest").as[List[String Map JsValue]].find(f => f("id").as[String] == dg._1).map ( x =>
				x - "drug_into" - "client_grade" - "type").getOrElse(throw new Exception("is null"))

			val goods = dg._2.map { g =>
				(data \ "current" \ "connect_goods").as[List[String Map JsValue]].find(f => g("goods_id").as[String] == f("id").as[String]).map ( x =>
					x - "drug_into" - "type").getOrElse(throw new Exception("is null"))
			}

			hospital ++ Map("med" -> toJson(goods))

		}.toList
		println(toJson(a))
		Nil
	}


	val formatHosptial: String Map JsValue => (Option[String Map JsValue], Option[JsValue]) = { m =>

		filterZip(m("scenario"))(zfcDestGoods)
		(Some(Map("a" -> toJson(1))), None)
	}



}
