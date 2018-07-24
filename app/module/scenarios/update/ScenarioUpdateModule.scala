package module.scenarios.update

import com.mongodb.DBObject
import module.common.processor
import module.common.processor._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import module.common.stragety.impl
import module.scenarios.ProposalMessage._
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.bmmessages.{CommonModules, MessageDefines}

object ScenarioUpdateModule extends ModuleTrait {

    val us: updateScenario = impl[updateScenario]
    import us._

    val success_result: DBObject => Map[String, JsValue] = { _ => Map("result" -> toJson("update success")) }

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])
                   (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_updateDGR(data) =>
            processor (value => returnValue(update(value)(qc, upDGR, success_result)))(data)
        case msg_current2past(data) =>
            processor (value => returnValue(update(value)(qc, c2p, success_result)))(data)
        case _ => ???
    }
}

