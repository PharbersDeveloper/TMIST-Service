package module.scenarios

import module.common.processor
import module.common.processor._
import play.api.libs.json.JsValue
import module.common.stragety.impl
import module.common.MergeStepResult
import play.api.libs.json.Json.toJson
import module.scenarios.ProposalMessage._
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.pharbersmacro.CURDMacro._
import com.pharbers.bmmessages.{CommonModules, MessageDefines}

object ScenarioModule extends ModuleTrait {
    val s: scenario = impl[scenario]
    import s._

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])
                   (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_queryScenarios(data) =>
            val sortCond = Some(pr.get ++ Map("sort" -> toJson("timestape")))
            processor (value => returnValue(queryMulti(value)(names)(qc, dr, cm).head, name))(MergeStepResult(data, sortCond))
        case msg_queryHospsByScenario(data) => ???
        case msg_queryResosByScenario(data) => (pr, None)
        case msg_queryGoodsByScenario(data) => (pr, None)

        case _ => ???
    }

}
