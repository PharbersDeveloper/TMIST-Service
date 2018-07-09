package controllers

import play.api.mvc._
import javax.inject.Inject
import akka.actor.ActorSystem
import play.api.libs.json.Json.toJson
import module.report.ReportMessage._
import controllers.common.requestArgsQuery
import com.pharbers.bmpattern.LogMessage.msg_log
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.bmpattern.ResultMessage.msg_CommonResultMessage

class ReportController @Inject()(implicit as_inject: ActorSystem, dbt: dbInstanceManager, cc: ControllerComponents)
		extends AbstractController(cc) {

	import com.pharbers.bmpattern.LogMessage.common_log
	import com.pharbers.bmpattern.ResultMessage.common_result

	def getTotalReport() = Action { request =>
		requestArgsQuery().requestArgs(request) { jv =>
			MessageRoutes(msg_log(toJson(Map("method" -> toJson("push new checkpoint"))), jv)
				:: msg_getTotalReport(jv)
				:: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt))))
		}
	}

	def getHospProdReport() = Action { request =>
		requestArgsQuery().requestArgs(request) { jv =>
			MessageRoutes(msg_log(toJson(Map("method" -> toJson("push new checkpoint"))), jv)
				:: msg_getHospProdReport(jv)
				:: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt))))
		}
	}

	def getRepProdReport() = Action { request =>
		requestArgsQuery().requestArgs(request) { jv =>
			MessageRoutes(msg_log(toJson(Map("method" -> toJson("get humans info"))), jv)
					:: msg_getRepProdReport(jv)
					:: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt))))
		}
	}

	def getResourceIO() = Action { request =>
		requestArgsQuery().requestArgs(request) { jv =>
			MessageRoutes(msg_log(toJson(Map("method" -> toJson("get hosp detail"))), jv)
					:: msg_getResourceIO(jv)
					:: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt))))
		}
	}

	def getRepIndResources() = Action { request =>
		requestArgsQuery().requestArgs(request) { jv =>
			MessageRoutes(msg_log(toJson(Map("method" -> toJson("get hosp detail"))), jv)
					:: msg_getRepIndResources(jv)
					:: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt))))
		}
	}

}