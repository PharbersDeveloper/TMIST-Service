package module.proposals

import com.pharbers.bmmessages.CommonMessage
import play.api.libs.json.JsValue

/**
  * Created by clock on 18-6-11.
  */
abstract class msg_ProposalCommand extends CommonMessage("proposal", ProposalModule)

object CheckpointMessage {
    case class msg_pushCheckpoint(data: JsValue) extends msg_ProposalCommand
    case class msg_popCheckpoint(data : JsValue) extends msg_ProposalCommand
    case class msg_updateCheckpoint(data : JsValue) extends msg_ProposalCommand
    case class msg_queryCheckpoint(data : JsValue) extends msg_ProposalCommand
    case class msg_farmatProposalName(data : JsValue) extends msg_ProposalCommand
}