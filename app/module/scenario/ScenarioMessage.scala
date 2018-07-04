package module.scenario

import play.api.libs.json.JsValue
import com.pharbers.bmmessages.CommonMessage

/**
  * Created by clock on 18-6-11.
  */
abstract class msg_ScenarioCommand extends CommonMessage("scenario", ScenarioModule)

object CheckpointMessage {
    case class msg_getHospLst(data: JsValue) extends msg_ScenarioCommand
    case class msg_getBudgetInfo(data : JsValue) extends msg_ScenarioCommand
    case class msg_getHumansInfo(data : JsValue) extends msg_ScenarioCommand
    case class msg_getHospDetail(data : JsValue) extends msg_ScenarioCommand
}