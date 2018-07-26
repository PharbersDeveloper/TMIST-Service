package controllers

import javax.inject.Inject
import akka.actor.ActorSystem
import module.users.UserMessage._
import play.api.libs.json.Json.toJson
import controllers.common.requestArgsQuery
import com.pharbers.driver.PhRedisDriverImpl
import com.pharbers.bmpattern.LogMessage.msg_log
import play.api.mvc.{AbstractController, ControllerComponents}
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.bmpattern.ResultMessage.msg_CommonResultMessage
import controllers.common.JsonapiAdapter.msg_JsonapiAdapter

class UserController @Inject()(implicit cc: ControllerComponents, as_inject: ActorSystem,
                               dbt: dbInstanceManager, rd: PhRedisDriverImpl)
        extends AbstractController(cc) {

    import com.pharbers.bmpattern.LogMessage.common_log
    import com.pharbers.bmpattern.ResultMessage.common_result
    import controllers.common.JsonapiAdapter.jsonapi_adapter

    // TODO 未测试
    def pushUser = Action(request => requestArgsQuery().requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("push new user"))), jv)
//                :: msg_verifyUserRegister(jv)
//                :: msg_queryRegisterCompany(jv)
//                :: msg_pushUser(jv)
//                :: msg_bindUserCompanyPre(jv)
//                :: msg_bindUserCompany(jv)
//                :: msg_registerUserForEm(jv)
//                :: msg_userJoinChatgroupForEm(jv)
                :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
    })

    def popUser = Action(request => requestArgsQuery().requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("pop user"))), jv)
                :: msg_popUser(jv)
                :: msg_JsonapiAdapter(jv)
                :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
    })

    def queryUserMulti = Action(request => requestArgsQuery().requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("query user multi"))), jv)
                :: msg_queryUserMulti(jv)
                :: msg_JsonapiAdapter(jv)
                :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
    })

    def userCompany = Action(request => requestArgsQuery().requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("user jobs"))), jv)
                :: msg_queryUser(jv)
                :: msg_userCompanyInfo(jv)
                :: msg_JsonapiAdapter(jv)
                :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
    })

    def userRoles = Action(request => requestArgsQuery().requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("user roles"))), jv)
                :: msg_queryUser(jv)
                :: msg_userRolesInfo(jv)
                :: msg_JsonapiAdapter(jv)
                :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
    })

    def userDetail = Action(request => requestArgsQuery().requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("query user"))), jv)
                :: msg_queryUser(jv)
                :: msg_userRolesInfo(jv)
                :: msg_userCompanyInfo(jv)
                :: msg_JsonapiAdapter(jv)
                :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
    })

    def userLogin = Action(request => requestArgsQuery().requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("user login"))), jv)
                :: msg_test(jv)
//                :: msg_authWithPassword(jv)
//                :: msg_userRolesInfo(jv)
//                :: msg_userCompanyInfo(jv)
//                :: msg_authSetExpire(jv)
//                :: msg_JsonapiAdapter(jv)
                :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
    })
}
