package module.scenarios

import module.common.processor._
import play.api.libs.json.JsValue
import module.common.stragety.impl
import module.scenarios.ProposalMessage._
import com.pharbers.bmpattern.ModuleTrait
import module.common.{MergeStepResult, processor}
import com.pharbers.pharbersmacro.CURDMacro.queryMacro
import com.pharbers.bmmessages.{CommonModules, MessageDefines}

object ScenarioQueryModule extends ModuleTrait {
    val s: scenario = impl[scenario]
    import s._

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])
                   (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_queryScenariosByUUID(data) => queryMacro(qc, dr, MergeStepResult(data, pr), names, name)
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
