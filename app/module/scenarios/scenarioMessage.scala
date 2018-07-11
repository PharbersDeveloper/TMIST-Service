package module.scenarios

import play.api.libs.json.JsValue
import com.pharbers.bmmessages.CommonMessage

/**
  * Created by clock on 18-7-11.
  */
abstract class msg_ScenarioCommand extends CommonMessage("scenario", ScenarioModule)

object ProposalMessage {
    case class msg_queryScenarios(data: JsValue) extends msg_ScenarioCommand
}