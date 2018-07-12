package module.scenarios.format

import play.api.libs.json.JsValue
import module.scenarios.ProposalMessage._
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.bmmessages.{CommonModules, MessageDefines}

object ScenarioFormatModule extends ModuleTrait with ParsingTrait {
	
    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])
                   (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_formatQueryHospLst(data) => ps2m(pr)(formatHospitals)
        case msg_formatQueryBudget(data) => ps2m(pr)(formatBudget)
        case msg_formatQueryHumans(data) => ps2m(pr)(formatHumans)
        case msg_formatQueryHospitalDetails(data) => ps2m(pr)(formatHospitalDetails)
        case _ => ???
    }
}

