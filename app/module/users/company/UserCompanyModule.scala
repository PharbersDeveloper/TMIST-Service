package module.users.company

import module.common.processor._
import module.users.UserMessage._
import play.api.libs.json.JsValue
import module.common.stragety.impl
import com.pharbers.bmpattern.ModuleTrait
import module.common.{MergeStepResult, processor}
import com.pharbers.bmmessages.{CommonModules, MessageDefines}

object UserCompanyModule extends ModuleTrait {
    val uc: user2company = impl[user2company]

    import uc._

    override def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])
                            (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_bindUserCompany(data) =>
            processor(value => returnValue(bindConnection(value)("user_company")))(MergeStepResult(data, pr))
        case msg_unbindUserCompany(data) =>
            processor(value => returnValue(unbindConnection(value)("user_company")))(MergeStepResult(data, pr))
        case msg_userCompanyInfo(data) =>
            processor(value => returnValue(queryConnection(value)(pr)("user_company")))(MergeStepResult(data, pr))

        case _ => ???
    }
}
