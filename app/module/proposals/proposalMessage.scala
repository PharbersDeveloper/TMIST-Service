package module.proposals

import com.pharbers.bmmessages.CommonMessage
import play.api.libs.json.JsValue

/**
  * Created by clock on 18-6-11.
  */
abstract class msg_ProposalCommand extends CommonMessage("proposal", ProposalModule)

object ProposalMessage {
    case class msg_pushProposal(data: JsValue) extends msg_ProposalCommand
    case class msg_popProposal(data : JsValue) extends msg_ProposalCommand
    case class msg_updateProposal(data : JsValue) extends msg_ProposalCommand
    case class msg_queryProposal(data : JsValue) extends msg_ProposalCommand
    case class msg_farmatProposalName(data : JsValue) extends msg_ProposalCommand


    case class msg_getHospLst(data: JsValue) extends msg_ProposalCommand
    case class msg_getBudgetInfo(data : JsValue) extends msg_ProposalCommand
    case class msg_getHumansInfo(data : JsValue) extends msg_ProposalCommand
    case class msg_getHospDetail(data : JsValue) extends msg_ProposalCommand
}