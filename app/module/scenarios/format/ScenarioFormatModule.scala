package module.scenarios.format

import play.api.libs.json.{JsObject, JsValue}
import play.api.libs.json.Json.toJson
import module.scenarios.ProposalMessage._
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.bmmessages.{CommonModules, MessageDefines}

object ScenarioFormatModule extends ModuleTrait with ParsingTrait {
	
    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])
                   (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_formatQueryHospLst(_) => ps2m(pr)(formatHospitals)
        case msg_formatQueryBudget(_) => ps2m(pr)(formatBudget)
        case msg_formatQueryHumans(_) => ps2m(pr)(formatHumans)
        case msg_formatQueryHospitalDetails(data) =>
			ps2m(pr.map( _ ++: data.as[JsObject].value.toMap).orElse(pr))(formatHospitalDetails)
        case _ => ???
    }
}

