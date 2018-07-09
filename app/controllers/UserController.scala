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

class UserController @Inject()(implicit cc: ControllerComponents, as_inject: ActorSystem,
                               dbt: dbInstanceManager, rd: PhRedisDriverImpl)
        extends AbstractController(cc) {

    import com.pharbers.bmpattern.LogMessage.common_log
    import com.pharbers.bmpattern.ResultMessage.common_result

//    // TODO 未测试
//    def pushUser = Action(request => requestArgsQuery().requestArgs(request) { jv =>
//        MessageRoutes(msg_log(toJson(Map("method" -> toJson("push new user"))), jv)
////                :: msg_verifyUserRegister(jv)
////                :: msg_queryRegisterCompany(jv)
////                :: msg_pushUser(jv)
////                :: msg_bindUserCompanyPre(jv)
////                :: msg_bindUserCompany(jv)
////                :: msg_registerUserForEm(jv)
////                :: msg_userJoinChatgroupForEm(jv)
//                :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt))))
//    })
//
//    // TODO 未测试
//    def popUser = Action(request => requestArgsQuery().requestArgs(request) { jv =>
//        MessageRoutes(msg_log(toJson(Map("method" -> toJson("pop user"))), jv)
////                :: msg_pushUser(jv)
////                :: msg_popUser(jv)
//                :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt))))
//    })
//
//    // TODO 未测试
//    def queryUserMulti = Action(request => requestArgsQuery().requestArgs(request) { jv =>
//        MessageRoutes(msg_log(toJson(Map("method" -> toJson("query user multi"))), jv)
//                :: msg_queryUserMulti(jv)
//                :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt))))
//    })
//
//    // TODO 未测试
//    def userDetail = Action(request => requestArgsQuery().requestArgs(request) { jv =>
//        MessageRoutes(msg_log(toJson(Map("method" -> toJson("user detail"))), jv)
//                :: msg_queryUser(jv)
//                :: msg_expendCompanyInfo(jv)
//                :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt))))
//    })
//
//    // TODO 未测试
//    def userJobs = Action(request => requestArgsQuery().requestArgs(request) { jv =>
//        MessageRoutes(msg_log(toJson(Map("method" -> toJson("user jobs"))), jv)
//                :: msg_queryUser(jv)
//                :: msg_expendJobsInfo(jv)
//                :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt))))
//    })
//
//    // TODO 未测试
//    def userRoles = Action(request => requestArgsQuery().requestArgs(request) { jv =>
//        MessageRoutes(msg_log(toJson(Map("method" -> toJson("user roles"))), jv)
//                :: msg_queryUser(jv)
//                :: msg_expendRolesInfo(jv)
//                :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt))))
//    })

    def queryUser = Action(request => requestArgsQuery().requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("query user"))), jv)
                :: msg_queryUser(jv)
                :: msg_userRolesInfo(jv)
                :: msg_userCompanyInfo(jv)
                :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt))))
    })

    def userLogin = Action(request => requestArgsQuery().requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("user login"))), jv)
                :: msg_authWithPassword(jv)
                :: msg_userRolesInfo(jv)
                :: msg_userCompanyInfo(jv)
                :: msg_authSetExpire(jv)
                :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "rd" -> rd))))
    })
}
