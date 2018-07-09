//package controllers.common
//
//import com.pharbers.ErrorCode
//import com.pharbers.bmmessages.{CommonMessage, CommonModules, MessageDefines}
//import com.pharbers.bmpattern.LogMessage.msg_log
//import com.pharbers.bmpattern.{LogModule, ModuleTrait, msg_LogCommand, msg_ResultCommand}
//import controllers.common.JsonapiAdapter.msg_jsonapiAdapter
//import org.apache.log4j.Logger
//import play.api.libs.json.JsValue
//import play.api.libs.json.Json.toJson
//
//
//object JsonapiAdapter {
//    implicit val common_log : (JsValue, (Option[Map[String, JsValue]], Option[JsValue])) => (Option[Map[String, JsValue]], Option[JsValue]) = (jv, pr) => {
//        try {
//            val logger = Logger.getRootLogger
//
//            val user_id = (data \ "user_id").asOpt[String].map (x => x).getOrElse("unknown user")
//            val method = (ls \ "method").asOpt[String].map (x => x).getOrElse(throw new Exception("log struct error"))
//
//            logger.info(s"$user_id call $method with args ${data.toString}")
//            (Some(Map("status" -> toJson("ok"))), None)
//
//        } catch {
//            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
//        }
//    }
//
//    abstract class msg_jsonapiCommand extends CommonMessage("jsonapiAdapt", JsonapiAdapterModule)
//    case class msg_jsonapiAdapter(data: JsValue)(implicit val fun : (JsValue, (Option[Map[String, JsValue]], Option[JsValue])) => (Option[Map[String, JsValue]], Option[JsValue])) extends msg_jsonapiCommand
//}
//
//object JsonapiAdapterModule extends ModuleTrait {
//    def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
//        case cmd : msg_jsonapiAdapter => cmd.fun(cmd.data, pr)
//        case _ => (None, Some(ErrorCode.errorToJson("can not parse result")))
//    }
//}