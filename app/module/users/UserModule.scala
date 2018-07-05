package module.users

import module.common.processor._
import module.users.UserMessage._
import play.api.libs.json.JsValue
import module.common.stragety.impl
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.pharbersmacro.CURDMacro._
import module.common.{MergeStepResult, processor}
import com.pharbers.bmmessages.{CommonModules, MessageDefines}

object UserModule extends ModuleTrait {
    val u: user = impl[user]

    import u._

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])
                   (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_verifyUserRegister(data) =>
            processor(value => returnValue(checkExist(value, pr, "user email has been use")(ckAttrExist, ssr, names)))(data)
        case msg_pushUser(data) => pushMacro(d2m, ssr, MergeStepResult(data, pr), names, name)
        case msg_popUser(data) => popMacro(qc, popr, data, names)
        case msg_queryUser(data) => queryMacro(qc, dr, MergeStepResult(data, pr), names, name)
        case msg_queryUserMulti(data) => queryMultiMacro(qcm, sr, MergeStepResult(data, pr), names, names)

        case _ => ???
    }
}
