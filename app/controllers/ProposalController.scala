package controllers

import play.api.mvc._
import javax.inject.Inject
import akka.actor.ActorSystem
import play.api.libs.json.Json.toJson
import module.proposals.ProposalMessage._
import controllers.common.requestArgsQuery
import com.pharbers.driver.PhRedisDriverImpl
import com.pharbers.bmpattern.LogMessage.msg_log
import com.pharbers.dbManagerTrait.dbInstanceManager
import controllers.common.JsonapiAdapter.msg_JsonapiAdapter
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.bmpattern.ResultMessage.msg_CommonResultMessage
import module.users.UserMessage.{msg_authParseToken, msg_userProposalInfo}

class ProposalController @Inject()(implicit cc: ControllerComponents, as_inject: ActorSystem,
                                   dbt: dbInstanceManager, rd: PhRedisDriverImpl)
        extends AbstractController(cc) {

    import com.pharbers.bmpattern.LogMessage.common_log
    import com.pharbers.bmpattern.ResultMessage.common_result
    import controllers.common.JsonapiAdapter.jsonapi_adapter

    def pushProposal() = Action { request =>
        requestArgsQuery().requestArgs(request) { jv =>
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("push new proposal"))), jv)
                    :: msg_authParseToken(jv)
                    :: msg_pushProposal(jv)
                    :: msg_JsonapiAdapter(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
        }
    }

    def popProposal() = Action { request =>
        requestArgsQuery().requestArgs(request) { jv =>
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("pop proposal"))), jv)
                    :: msg_authParseToken(jv)
                    :: msg_popProposal(jv)
                    :: msg_JsonapiAdapter(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
        }
    }

    def updateProposal() = Action { request =>
        requestArgsQuery().requestArgs(request) { jv =>
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("update proposal"))), jv)
                    :: msg_authParseToken(jv)
                    :: msg_updateProposal(jv)
                    :: msg_JsonapiAdapter(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
        }
    }

    def queryProposal() = Action { request =>
        requestArgsQuery().requestArgs(request) { jv =>
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("query proposal"))), jv)
                    :: msg_authParseToken(jv)
                    :: msg_queryProposal(jv)
                    :: msg_JsonapiAdapter(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
        }
    }

    def queryProposalLst() = Action { request =>
        requestArgsQuery().requestArgs(request) { jv =>
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("query multi proposal"))), jv)
                    :: msg_authParseToken(jv)
                    :: msg_userProposalInfo(jv)
                    :: msg_formatProposalName(jv)
                    :: msg_JsonapiAdapter(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
        }
    }

    // TODO 未完成
    def queryHospLst() = Action { request =>
        requestArgsQuery().requestArgs(request) { jv =>
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("query hosp lst in proposal"))), jv)
                    :: msg_authParseToken(jv)
                    :: msg_queryProposal(jv)
                    :: msg_queryScenariosByProposal(jv)
                    :: msg_getFirstScenario(jv)
                    :: msg_queryHospsByScenario(jv)
                    :: msg_queryRepsByHosp(jv)
                    :: msg_queryProdsByHosp(jv)
                    :: msg_JsonapiAdapter(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
        }
    }

    // TODO 未完成
    def queryBudgetInfo() = Action { request =>
        requestArgsQuery().requestArgs(request) { jv =>
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("query budget info in proposal"))), jv)
                    :: msg_getBudgetInfo(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
        }
    }

    // TODO 未完成
    def queryHumansInfo() = Action { request =>
        requestArgsQuery().requestArgs(request) { jv =>
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("query humans info in proposal"))), jv)
                    :: msg_getHumansInfo(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
        }
    }

    def getHospDetail() = Action { request =>
        requestArgsQuery().requestArgs(request) { jv =>
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("get hosp detail"))), jv)
                    :: msg_getHospDetail(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
        }
    }

    def getTotalReport() = Action { request =>
        requestArgsQuery().requestArgs(request) { jv =>
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("push new checkpoint"))), jv)
                    :: msg_getTotalReport(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
        }
    }

    def getHospProdReport() = Action { request =>
        requestArgsQuery().requestArgs(request) { jv =>
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("push new checkpoint"))), jv)
                    :: msg_getHospProdReport(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
        }
    }

    def getRepProdReport() = Action { request =>
        requestArgsQuery().requestArgs(request) { jv =>
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("get humans info"))), jv)
                    :: msg_getRepProdReport(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
        }
    }

    def getResourceIO() = Action { request =>
        requestArgsQuery().requestArgs(request) { jv =>
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("get hosp detail"))), jv)
                    :: msg_getResourceIO(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
        }
    }

    def getRepIndResources() = Action { request =>
        requestArgsQuery().requestArgs(request) { jv =>
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("get hosp detail"))), jv)
                    :: msg_getRepIndResources(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
        }
    }

}