package module.users.proposal

import module.common.processor._
import module.users.UserMessage._
import module.proposals.proposal
import play.api.libs.json.JsValue
import module.common.stragety.impl
import com.pharbers.bmpattern.ModuleTrait
import module.common.{MergeStepResult, processor}
import com.pharbers.bmmessages.{CommonModules, MessageDefines}

/**
  * Created by spark on 18-7-9.
  */
object UserProposalModule extends ModuleTrait {
    val p: proposal = impl[proposal]
    val up: user2proposal = impl[user2proposal]
    import up._

    override def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])
                            (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_bindUserProposal(data) =>
            processor(value => returnValue(bindConnection(value)("bind_user_proposal")))(MergeStepResult(data, pr))
        case msg_unbindUserProposal(data) =>
            processor(value => returnValue(unbindConnection(value)("bind_user_proposal")))(MergeStepResult(data, pr))
        case msg_userProposalInfo(data) =>
            processor(value => returnValue(queryConnection(value)(pr)(p.sr)("bind_user_proposal")))(MergeStepResult(data, pr))

        case _ => ???
    }
}
