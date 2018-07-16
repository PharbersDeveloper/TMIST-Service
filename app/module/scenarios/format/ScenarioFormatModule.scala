package module.scenarios.format

import play.api.libs.json.{JsObject, JsValue}
import module.scenarios.ProposalMessage._
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.bmmessages.{CommonModules, MessageDefines}

object ScenarioFormatModule extends ModuleTrait with FormatScenarioTrait {
	
    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])
                   (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_formatQueryHospLst(_) => format(pr)(formatHospitals)
        case msg_formatQueryBudget(_) => format(pr)(formatBudget)
        case msg_formatQueryHumans(_) => format(pr)(formatHumans)
        case msg_formatQueryHospitalDetails(data) =>
            format(pr.map( _ ++: data.as[JsObject].value.toMap).orElse(pr))(formatHospitalDetails)
        case _ => ???
    }
}

