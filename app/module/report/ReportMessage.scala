package module.report

import play.api.libs.json.JsValue
import com.pharbers.bmmessages.CommonMessage

/**
  * Created by clock on 18-6-11.
  */
abstract class msg_ReportCommand extends CommonMessage("report", ReportModule)

object ReportMessage {
    case class msg_getTotalReport(data: JsValue) extends msg_ReportCommand
    case class msg_getHospProdReport(data : JsValue) extends msg_ReportCommand
    case class msg_getRepProdReport(data : JsValue) extends msg_ReportCommand
    case class msg_getResourceIO(data : JsValue) extends msg_ReportCommand
    case class msg_getRepIndResources(data : JsValue) extends msg_ReportCommand
}