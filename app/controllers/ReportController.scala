package controllers

import play.api.mvc._
import javax.inject.Inject
import akka.actor.ActorSystem
import play.api.libs.json.Json.toJson
import module.reports.ProposalMessage._
import controllers.common.requestArgsQuery
import com.pharbers.driver.PhRedisDriverImpl
import com.pharbers.bmpattern.LogMessage.msg_log
import module.users.UserMessage.msg_authParseToken
import com.pharbers.dbManagerTrait.dbInstanceManager
import controllers.common.JsonapiAdapter.msg_JsonapiAdapter
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.bmpattern.ResultMessage.msg_CommonResultMessage

class ReportController @Inject()(implicit cc: ControllerComponents, as_inject: ActorSystem,
                                 dbt: dbInstanceManager, rd: PhRedisDriverImpl)
        extends AbstractController(cc) {

    import com.pharbers.bmpattern.LogMessage.common_log
    import com.pharbers.bmpattern.ResultMessage.common_result
    import controllers.common.JsonapiAdapter.jsonapi_adapter

    def queryTotalReport() = Action { request =>
        requestArgsQuery().requestArgs(request) { jv =>
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("query total report"))), jv)
                    :: msg_authParseToken(jv)
                    :: msg_queryReport(jv)
                    :: msg_queryTotalReport(jv)
                    :: msg_JsonapiAdapter(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
        }
    }

    def queryDestsGoodsReport() = Action { request =>
        requestArgsQuery().requestArgs(request) { jv =>
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("query dests goods RelationReport"))), jv)
                    :: msg_authParseToken(jv)
                    :: msg_queryReport(jv)
                    :: msg_queryDestsGoodsReport(jv)
                    :: msg_JsonapiAdapter(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
        }
    }

    def queryResosGoodsReport() = Action { request =>
        requestArgsQuery().requestArgs(request) { jv =>
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("query resourse goods RelationReport"))), jv)
                    :: msg_authParseToken(jv)
                    :: msg_queryReport(jv)
                    :: msg_queryResosGoodsReport(jv)
                    :: msg_JsonapiAdapter(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
        }
    }

    def queryResosIO() = Action { request =>
        requestArgsQuery().requestArgs(request) { jv =>
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("query resourse intput output RelationReport"))), jv)
                    :: msg_authParseToken(jv)
                    :: msg_queryReport(jv)
                    :: msg_queryResosIO(jv)
                    :: msg_JsonapiAdapter(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
        }
    }

    def queryRepIndResos() = Action { request =>
        requestArgsQuery().requestArgs(request) { jv =>
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("query representative indicators resourse RelationReport"))), jv)
                    :: msg_authParseToken(jv)
                    :: msg_queryReport(jv)
                    :: msg_queryRepIndResos(jv)
                    :: msg_JsonapiAdapter(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
        }
    }

}