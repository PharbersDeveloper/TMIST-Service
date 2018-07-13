package module.proposals

import ProposalMessage._
import module.users.user
import module.common.processor._
import module.roles.RoleMessage._
import play.api.libs.json.JsValue
import module.common.stragety.impl
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.pharbersmacro.CURDMacro._
import module.common.{MergeStepResult, processor}
import com.pharbers.bmmessages.{CommonModules, MessageDefines}

object ProposalModule extends ModuleTrait {
    val u: user = impl[user]
    val p: proposal = impl[proposal]
    val p2u: proposal2user = impl[proposal2user]
    import p._
    import p2u._

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])
                   (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_pushProposal(data) => pushMacro(d2m, ssr, data, names, name)
        case msg_popRole(data) => popMacro(qc, popr, data, names)
        case msg_updateProposal(data) => updateMacro(qc, up2m, dr, data, names, name)
        case msg_queryProposal(data) => queryMacro(qc, dr, MergeStepResult(data, pr), names, name)
        case msg_queryUsersByProposal(data) =>
            processor(value => returnValue(queryConnection(value)(pr)(u.sr)("bind_user_proposal")))(MergeStepResult(data, pr))
        case msg_formatProposalName(data) =>
            processor (_ => (Some(Map("result" -> pr.get(u.name).asOpt[Map[String, JsValue]].get(p.names))), None))(data)
        case _ => ???
    }

}
