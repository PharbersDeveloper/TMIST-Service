package module.roles

import module.users.user
import module.common.processor._
import module.roles.RoleMessage._
import play.api.libs.json.JsValue
import module.common.stragety.impl
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.pharbersmacro.CURDMacro._
import module.common.{MergeStepResult, processor}
import com.pharbers.bmmessages.{CommonModules, MessageDefines}

/**
  * Created by clock on 18-7-6.
  */
object RoleModule extends ModuleTrait {
    val r: role = impl[role]
    val u: user = impl[user]
    val r2u: role2user = impl[role2user]
    import r._
    import r2u._

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])
                   (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_verifyRoleRegister(data) =>
            processor(value => returnValue(checkExist(value, pr, "role info has been use")(ckAttrExist, ssr, names)))(data)
        case msg_pushRole(data) => pushMacro(d2m, dr, data, names, name)
        case msg_popRole(data) => popMacro(qc, popr, data, names)
        case msg_updateRole(data) => updateMacro(qc, up2m, dr, data, names, name)
        case msg_queryRole(data) => queryMacro(qc, dr, data, names, name)
        case msg_queryRoleMulti(data) => queryMultiMacro(qcm, dr, data, names, names)
        case msg_queryUsersByRole(data) =>
            processor(value => returnValue(queryConnection(value)(pr)(u.sr)("bind_user_role")))(MergeStepResult(data, pr))
        case _ => ???
    }

}
