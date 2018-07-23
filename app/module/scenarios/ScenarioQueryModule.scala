package module.scenarios

import module.common.processor
import module.common.processor._
import play.api.libs.json.JsValue
import module.common.stragety.impl
import module.common.MergeStepResult
import play.api.libs.json.Json.toJson
import module.scenarios.ProposalMessage._
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.bmmessages.{CommonModules, MessageDefines}

object ScenarioQueryModule extends ModuleTrait {
    val s: scenario = impl[scenario]
    import s._

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])
                   (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_queryScenarios(data) =>
            val sortCond = Some(pr.get ++ Map("sort" -> toJson("timestamp")))
            processor (value => returnValue(queryMulti(value)(names)(qc, dr, cm).last, name))(MergeStepResult(data, sortCond))
        case msg_queryHospsByScenario(data) =>
            processor (_ => returnValue(queryConnectData(pr)("connect_dest")("dests")(dr)))(data)
        case msg_queryRepsByScenario(data) =>
            processor (_ => returnValue(queryConnectData(pr)("connect_rep")("representatives")(dr)))(data)
        case msg_queryResosByScenario(data) =>
            processor (_ => returnValue(queryConnectData(pr)("connect_reso")("resources")(dr)))(data)
        case msg_queryGoodsByScenario(data) =>
            processor (_ => returnValue(queryConnectData(pr)("connect_goods")("goods")(dr)))(data)

        case _ => ???
    }

}
