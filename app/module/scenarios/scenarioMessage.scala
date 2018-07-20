package module.scenarios

import play.api.libs.json.JsValue
import com.pharbers.bmmessages.CommonMessage
import module.scenarios.format.ScenarioFormatModule

/**
  * Created by clock on 18-7-11.
  */
abstract class msg_ScenarioCommand extends CommonMessage("scenario", ScenarioModule)
abstract class msg_ScenarioFormatCommand extends CommonMessage("scenario", ScenarioFormatModule)

object ProposalMessage {

    object msg_queryScenariosDetail {
        def apply(data: JsValue): List[msg_ScenarioCommand] =
            msg_queryScenarios(data) ::
                    msg_queryHospsByScenario(data) ::
                    msg_queryRepsByScenario(data) ::
                    msg_queryResosByScenario(data) ::
                    msg_queryGoodsByScenario(data) ::
                    Nil
    }

    case class msg_queryScenarios(data: JsValue) extends msg_ScenarioCommand
    case class msg_queryHospsByScenario(data: JsValue) extends msg_ScenarioCommand
    case class msg_queryRepsByScenario(data: JsValue) extends msg_ScenarioCommand
    case class msg_queryResosByScenario(data: JsValue) extends msg_ScenarioCommand
    case class msg_queryGoodsByScenario(data: JsValue) extends msg_ScenarioCommand

    case class msg_formatQueryHospLst(data: JsValue) extends msg_ScenarioFormatCommand
    case class msg_formatQueryBudget(data: JsValue) extends msg_ScenarioFormatCommand
    case class msg_formatQueryHumans(data: JsValue) extends msg_ScenarioFormatCommand
    case class msg_formatQueryHospitalDetails(data: JsValue) extends msg_ScenarioFormatCommand
}