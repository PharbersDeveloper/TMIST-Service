package module.scenarios

import play.api.libs.json.JsValue
import com.pharbers.bmmessages.CommonMessage
import module.scenarios.format.ScenarioFormatModule
import module.scenarios.update.ScenarioUpdateModule

/**
  * Created by clock on 18-7-11.
  */
abstract class msg_ScenarioQueryCommand extends CommonMessage("scenario", ScenarioQueryModule)
abstract class msg_ScenarioFormatCommand extends CommonMessage("scenario", ScenarioFormatModule)
abstract class msg_ScenarioUpdateCommand extends CommonMessage("scenario", ScenarioUpdateModule)

object ProposalMessage {

    object msg_queryScenariosDetail {
        def apply(data: JsValue): List[msg_ScenarioQueryCommand] =
            msg_queryScenariosByUUID(data) ::
                    msg_queryHospsByScenario(data) ::
                    msg_queryRepsByScenario(data) ::
                    msg_queryResosByScenario(data) ::
                    msg_queryGoodsByScenario(data) ::
                    Nil
    }

    case class msg_queryScenariosByUUID(data: JsValue) extends msg_ScenarioQueryCommand
    case class msg_queryHospsByScenario(data: JsValue) extends msg_ScenarioQueryCommand
    case class msg_queryRepsByScenario(data: JsValue) extends msg_ScenarioQueryCommand
    case class msg_queryResosByScenario(data: JsValue) extends msg_ScenarioQueryCommand
    case class msg_queryGoodsByScenario(data: JsValue) extends msg_ScenarioQueryCommand

    case class msg_formatQueryHospLst(data: JsValue) extends msg_ScenarioFormatCommand
    case class msg_formatQueryBudget(data: JsValue) extends msg_ScenarioFormatCommand
    case class msg_formatQueryHumans(data: JsValue) extends msg_ScenarioFormatCommand
    case class msg_formatQueryHospitalDetails(data: JsValue) extends msg_ScenarioFormatCommand

    case class msg_updateDGR(data: JsValue) extends msg_ScenarioUpdateCommand
    case class msg_current2past(data: JsValue) extends msg_ScenarioUpdateCommand
}