package module.users

import play.api.libs.json.JsValue
import module.users.auth.UserAuthModule
import com.pharbers.bmmessages.CommonMessage
import module.users.company.UserCompanyModule

/**
  * Created by spark on 18-4-19.
  */
abstract class msg_UserCommonCommand extends CommonMessage("users", UserModule)
abstract class msg_UserAuthCommand extends CommonMessage("users", UserAuthModule)
abstract class msg_UserCompanyCommand extends CommonMessage("users", UserCompanyModule)

object UserMessage {

    // 用户设置
    case class msg_verifyUserRegister(data: JsValue) extends msg_UserCommonCommand
    case class msg_pushUser(data: JsValue) extends msg_UserCommonCommand
    case class msg_popUser(data : JsValue) extends msg_UserCommonCommand
    case class msg_queryUser(data : JsValue) extends msg_UserCommonCommand
    case class msg_queryUserMulti(data : JsValue) extends msg_UserCommonCommand

    // 用户 公司
    case class msg_bindUserCompanyPre(data: JsValue) extends msg_UserCompanyCommand
    case class msg_bindUserCompany(data : JsValue) extends msg_UserCompanyCommand
    case class msg_unbindUserCompany(data : JsValue) extends msg_UserCompanyCommand
    case class msg_userCompanyInfo(data : JsValue) extends msg_UserCompanyCommand

    // 用户 权限
    case class msg_bindUserRole(data: JsValue) extends msg_UserAuthCommand
    case class msg_unbindUserRole(data : JsValue) extends msg_UserAuthCommand
    case class msg_userRolesInfo(data : JsValue) extends msg_UserAuthCommand
    case class msg_authWithPassword(data: JsValue) extends msg_UserAuthCommand
    case class msg_authSetExpire(data: JsValue) extends msg_UserAuthCommand
    case class msg_authParseToken(data: JsValue) extends msg_UserAuthCommand

}