package module.checkpoint

import module.roles.role
import module.common.processor._
import module.roles.RoleMessage._
import play.api.libs.json.Json.toJson
import play.api.libs.json.{JsValue, Json}
import com.pharbers.bmpattern.ModuleTrait
import module.checkpoint.CheckpointMessage._
import com.pharbers.pharbersmacro.CURDMacro._
import module.common.{MergeStepResult, processor}
import com.pharbers.bmmessages.{CommonModules, MessageDefines}

object CheckpointModule extends ModuleTrait {
    val role = new role()
    import role._
    val name = "checkppoint"
    val names = "checkppoints"

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])
                   (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_pushCheckpoint(data) => pushMacro(d2m, ssr, data, names, name)
        case msg_popRole(data) => popMacro(qc, popr, data, names)
        case msg_updateCheckpoint(data) => updateMacro(qc, up2m, dr, data, names, name)
        case msg_queryCheckpoint(data) => queryMacro(qc, dr, MergeStepResult(data, pr), names, name)
        case msg_queryCheckpointMulti(data) =>
            //queryMultiMacro(???, ???, MergeStepResult(data, pr), names, names)
            processor (value => getCheckpointMulti(value))(data)

        case _ => ???
    }

    def getCheckpointMulti(data: JsValue) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        val checkpoint = """
         |      {
         |            "type": "checkpoint",
         |            "attribute": [
         |                    {
         |                        "id": "1111",
         |                        "name": "关卡01"
         |                    },
         |                    {
         |                        "id": "2222",
         |                        "name": "关卡02"
         |                    }
         |              ]
         |        }
        """.stripMargin
        val hospLst = """
         |      {
         |            "type": "checkpoint",
         |            "attribute": {
         |    "currentMonth": "2",
         |    "hospitalList": [
         |        {
         |            "hospid": "111",
         |            "name": "中日医院",
         |            "level": "综合/三甲",
         |            "department": "皮肤科",
         |            "bed": 1000,
         |            "outpatient": 1234545,
         |            "surgery": 1000,
         |            "representive": {
         |                "name": "校长",
         |                "avatar": "/assets/images/hosp_seller.png"
         |            },
         |            "medicine": [
         |                {
         |                    "name": "口服抗生素",
         |                    "potential": "54,561,334",
         |                    "previoussales": "554,687",
         |                    "contributionrate": "5%",
         |                    "share": "12%"
         |                }
         |            ]
         |        }
         |    ]
         |}
         |        }
        """.stripMargin

        val version =
            """
              |{
              |     "major": 1,
              |     "minor": 0
              |}
            """.stripMargin

        (Some(Map(
            "timestamp" -> toJson(1530689119000L),
            "version" -> Json.parse(version),
            "data" -> Json.parse(hospLst)
        )), None)
    }

}
