package module.checkpoint

import play.api.libs.json.JsValue
import com.pharbers.bmmessages.CommonMessage

/**
  * Created by clock on 18-6-11.
  */
abstract class msg_CheckpointCommand extends CommonMessage("checkpoint", CheckpointModule)

object CheckpointMessage {
    case class msg_pushCheckpoint(data: JsValue) extends msg_CheckpointCommand
    case class msg_popCheckpoint(data : JsValue) extends msg_CheckpointCommand
    case class msg_updateCheckpoint(data : JsValue) extends msg_CheckpointCommand
    case class msg_queryCheckpoint(data : JsValue) extends msg_CheckpointCommand
    case class msg_queryCheckpointMulti(data : JsValue) extends msg_CheckpointCommand
}