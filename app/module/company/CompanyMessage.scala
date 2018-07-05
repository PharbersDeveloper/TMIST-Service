package module.company

import play.api.libs.json.JsValue
import com.pharbers.bmmessages.CommonMessage

/**
  * Created by clock on 18-7-6.
  */
abstract class msg_CompanyCommand extends CommonMessage("company", CompanyModule)

object CompanyMessage {
    case class msg_verifyCompanyRegister(data: JsValue) extends msg_CompanyCommand
    case class msg_pushCompany(data: JsValue) extends msg_CompanyCommand
    case class msg_popCompany(data : JsValue) extends msg_CompanyCommand
    case class msg_updateCompany(data : JsValue) extends msg_CompanyCommand
    case class msg_queryCompany(data : JsValue) extends msg_CompanyCommand

    case class msg_queryUsersByCompany(data : JsValue) extends msg_CompanyCommand

}