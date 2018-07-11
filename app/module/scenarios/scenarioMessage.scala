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
    case class msg_queryScenarios(data: JsValue) extends msg_ScenarioCommand
    case class msg_queryHospsByScenario(data: JsValue) extends msg_ScenarioCommand
    case class msg_queryResosByScenario(data: JsValue) extends msg_ScenarioCommand
    case class msg_queryGoodsByScenario(data: JsValue) extends msg_ScenarioCommand

    case class msg_formatQueryHospLst(data: JsValue) extends msg_ScenarioFormatCommand
}