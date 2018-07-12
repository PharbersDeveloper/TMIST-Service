package module.scenarios.format

import play.api.libs.json.JsValue
import play.api.libs.json.Json._
import module.scenarios.ProposalMessage._
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.bmmessages.{CommonModules, MessageDefines}

object ScenarioFormatModule extends ModuleTrait with ParsingTrait {
	
	val formatHospital: String Map JsValue => (Option[String Map JsValue], Option[JsValue]) = { m =>
		(Some(filterZip(m("scenario"))(zfcDestGoods)), None)
	}

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])
                   (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_formatQueryHospLst(data) => ps2m(pr)(formatHospital)
        case _ => ???
    }
}

