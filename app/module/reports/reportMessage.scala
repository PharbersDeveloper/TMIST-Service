package module.reports

import play.api.libs.json.JsValue
import com.pharbers.bmmessages.CommonMessage

/**
  * Created by clock on 18-7-13.
  */
abstract class msg_ReportCommand extends CommonMessage("report", ReportModule)

object ProposalMessage {
    case class msg_queryReport(data: JsValue) extends msg_ReportCommand

    case class msg_queryTotalReport(data: JsValue) extends msg_ReportCommand
    case class msg_queryDestsGoodsReport(data : JsValue) extends msg_ReportCommand
    case class msg_queryResosGoodsReport(data : JsValue) extends msg_ReportCommand
    case class msg_queryResosIO(data : JsValue) extends msg_ReportCommand
    case class msg_queryRepIndResos(data : JsValue) extends msg_ReportCommand
}