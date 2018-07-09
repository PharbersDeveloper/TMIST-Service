package module.company

import module.common.processor._
import play.api.libs.json.JsValue
import module.common.stragety.impl
import module.company.CompanyMessage._
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.pharbersmacro.CURDMacro._
import module.common.{MergeStepResult, processor}
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import module.users.user

/**
  * Created by clock on 18-7-6.
  */
object CompanyModule extends ModuleTrait {
    val c: company = impl[company]
    val u: user = impl[user]
    val c2u: company2user = impl[company2user]
    import c._
    import c2u._

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])
                   (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_verifyCompanyRegister(data) =>
            processor(value => returnValue(checkExist(value, pr, "company name has been use")(ckAttrExist, ssr, names)))(data)
        case msg_pushCompany(data) => pushMacro(d2m, dr, data, names, name)
        case msg_popCompany(data) => popMacro(qc, popr, data, names)
        case msg_updateCompany(data) => updateMacro(qc, up2m, dr, data, names, name)
        case msg_queryCompany(data) => queryMacro(qc, dr, data, names, name)
        case msg_queryUsersByCompany(data) =>
            processor(value => returnValue(queryConnection(value)(pr)(u.sr)("bind_user_company")))(MergeStepResult(data, pr))
        case _ => ???
    }

}
