package module.users.auth

import module.users.user
import module.common.processor._
import module.users.UserMessage._
import module.common.stragety.impl
import play.api.libs.json.JsValue
import com.pharbers.bmpattern.ModuleTrait
import module.common.{MergeStepResult, processor}
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import module.roles.role

object UserAuthModule extends ModuleTrait {
    val u: user = impl[user]
    val r: role = impl[role]
    val ur: user2role = impl[user2role]

    import u._
    import ur._

    override def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])
                            (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_bindUserRole(data) =>
            processor(value => returnValue(bindConnection(value)("bind_user_role")))(MergeStepResult(data, pr))
        case msg_unbindUserRole(data) =>
            processor(value => returnValue(unbindConnection(value)("bind_user_role")))(MergeStepResult(data, pr))
        case msg_userRolesInfo(data) =>
            processor(value => returnValue(queryConnection(value)(pr)(r.dr)("bind_user_role")))(MergeStepResult(data, pr))

        case msg_authWithPassword(data) =>
            processor(value => returnValue(authWithPassword(authPwd, sr)(value)(names), name))(data)
        case msg_authSetExpire(data) =>
            println(MergeStepResult(data, pr))
            processor(value => returnValue(authSetExpire(value)))(MergeStepResult(data, pr))
        case msg_authParseToken(data) =>
            processor(value => returnValue(authParseToken(value)))(data)

        case _ => ???
    }
}
