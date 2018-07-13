package module.proposals

import play.api.libs.json.JsValue
import com.pharbers.bmmessages.CommonMessage
import module.proposals.scenarios.ScenarioModule

/**
  * Created by clock on 18-6-11.
  */
abstract class msg_ProposalCommand extends CommonMessage("proposal", ProposalModule)

object ProposalMessage {
    case class msg_pushProposal(data: JsValue) extends msg_ProposalCommand
    case class msg_popProposal(data : JsValue) extends msg_ProposalCommand
    case class msg_updateProposal(data : JsValue) extends msg_ProposalCommand
    case class msg_queryProposal(data : JsValue) extends msg_ProposalCommand
    case class msg_queryUsersByProposal(data : JsValue) extends msg_ProposalCommand
    case class msg_formatProposalName(data : JsValue) extends msg_ProposalCommand



    case class msg_getTotalReport(data: JsValue) extends msg_ReportCommand
    case class msg_getHospProdReport(data : JsValue) extends msg_ReportCommand
    case class msg_getRepProdReport(data : JsValue) extends msg_ReportCommand
    case class msg_getResourceIO(data : JsValue) extends msg_ReportCommand
    case class msg_getRepIndResources(data : JsValue) extends msg_ReportCommand
}