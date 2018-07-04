package controllers

import akka.actor.ActorSystem
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.bmpattern.LogMessage.msg_log
import com.pharbers.bmpattern.ResultMessage.msg_CommonResultMessage
import com.pharbers.dbManagerTrait.dbInstanceManager
import controllers.common.requestArgsQuery
import javax.inject.Inject
import module.scenario.CheckpointMessage._
import play.api.libs.json.Json.toJson
import play.api.mvc._

class ScenarioController @Inject()(implicit as_inject: ActorSystem, dbt: dbInstanceManager, cc: ControllerComponents)
		extends AbstractController(cc) {

	import com.pharbers.bmpattern.LogMessage.common_log
	import com.pharbers.bmpattern.ResultMessage.common_result

	def getHospLst() = Action {
		Ok(views.html.index())
	}

	def getBudgetInfo() = Action {
		Ok(views.html.index())
	}

	def getHumansInfo() = Action { request =>
		requestArgsQuery().requestArgs(request) { jv =>
			MessageRoutes(msg_log(toJson(Map("method" -> toJson("get humans info"))), jv)
					:: msg_getHumansInfo(jv)
					:: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt))))
		}
	}

	def getHospDetail() = Action { request =>
		requestArgsQuery().requestArgs(request) { jv =>
			MessageRoutes(msg_log(toJson(Map("method" -> toJson("get hosp detail"))), jv)
					:: msg_getHospDetail(jv)
					:: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt))))
		}
	}

}