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
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt))))
        }
    }

    def popProposal() = Action { request =>
        requestArgsQuery().requestArgs(request) { jv =>
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("pop proposal"))), jv)
                    :: msg_authParseToken(jv)
                    :: msg_popProposal(jv)
                    :: msg_JsonapiAdapter(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt))))
        }
    }

    def updateProposal() = Action { request =>
        requestArgsQuery().requestArgs(request) { jv =>
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("update proposal"))), jv)
                    :: msg_authParseToken(jv)
                    :: msg_updateProposal(jv)
                    :: msg_JsonapiAdapter(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt))))
        }
    }

    def queryProposal() = Action { request =>
        requestArgsQuery().requestArgs(request) { jv =>
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("query proposal"))), jv)
                    :: msg_authParseToken(jv)
                    :: msg_queryProposal(jv)
                    :: msg_JsonapiAdapter(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt))))
        }
    }

    def queryProposalLst() = Action { request =>
        requestArgsQuery().requestArgs(request) { jv =>
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("query multi proposal"))), jv)
                    :: msg_authParseToken(jv)
                    :: msg_userProposalInfo(jv)
                    :: msg_farmatProposalName(jv)
                    :: msg_JsonapiAdapter(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
        }
    }

}