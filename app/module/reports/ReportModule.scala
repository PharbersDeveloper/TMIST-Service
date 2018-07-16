package module.reports

import module.common.processor
import module.common.processor._
import play.api.libs.json.Json.toJson
import module.reports.ProposalMessage._
import com.pharbers.bmpattern.ModuleTrait
import play.api.libs.json.{JsValue, Json}
import com.pharbers.bmmessages.{CommonModules, MessageDefines}

object ReportModule extends ModuleTrait {
    val report = new report()
    import report._

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])
                   (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_queryReport(data) =>
            processor (value => returnValue(query(value)(names)))(data)
        case msg_queryTotalReport(_) =>
            (Some(Map("result" -> pr.get("total_report"))), None)
        case msg_queryDestsGoodsReport(_) =>
            (Some(Map("result" -> pr.get("dests_goods_report"))), None)
        case msg_queryResosGoodsReport(_) =>
            (Some(Map("result" -> pr.get("resos_goods_report"))), None)
        case msg_queryResosIO(_) =>
            (Some(Map("result" -> pr.get("resos_io"))), None)
        case msg_queryRepIndResos(_) =>
            (Some(Map("result" -> pr.get("rep_ind_resos"))), None)

        case _ => ???
    }

}
