package module.scenario

import module.roles.role
import module.common.processor
import module.common.processor._
import play.api.libs.json.Json.toJson
import module.scenario.CheckpointMessage._
import play.api.libs.json.{JsValue, Json}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.pharbersmacro.CURDMacro._
import com.pharbers.bmmessages.{CommonModules, MessageDefines}

object ScenarioModule extends ModuleTrait {
    val role = new role()
    import role._
    val name = "checkppoint"
    val names = "checkppoints"

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])
                   (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_getHospLst(data) => pushMacro(d2m, ssr, data, names, name)
        case msg_getBudgetInfo(data) => popMacro(qc, popr, data, names)
        case msg_getHumansInfo(data) =>
            //updateMacro(qc, up2m, dr, data, names, name)
            processor (value => getHumansInfo(value))(data)
        case msg_getHospDetail(data) =>
            //queryMultiMacro(???, ???, MergeStepResult(data, pr), names, names)
            processor (value => getHospDetail(value))(data)

        case _ => ???
    }

    def getHumansInfo(data: JsValue) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        val humansInfo = """
         |      {
         |            "type": "checkpoint",
         |            "attribute": [
         |    {
         |        "name": "代表一",
         |        "total": 30,
         |        "used": 20,
         |        "percentage": 67
         |    },
         |    {
         |        "name": "代表二",
         |        "total": 30,
         |        "used": 29,
         |        "percentage": 96
         |    },
         |    {
         |        "name": "代表三",
         |        "total": 30,
         |        "used": 20,
         |        "percentage": 67
         |    },
         |    {
         |        "name": "代表四",
         |        "total": 30,
         |        "used": 15,
         |        "percentage": 50
         |    },
         |    {
         |        "name": "代表五",
         |        "total": 30,
         |        "used": 10,
         |        "percentage": 33
         |    }
         |]
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
            "data" -> Json.parse(humansInfo)
        )), None)
    }

    def getHospDetail(data: JsValue) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        val hospDetail = """
         |      {
         |            "type": "checkpoint",
         |            "attribute": {
         |    "medicines": [
         |        {
         |            "id": "medicine000",
         |            "name": "口服抗生素",
         |            "marketpotential": 333444555,
         |            "potentialgrowth": 99,
         |            "previoussales": 555444,
         |            "previousgrowth": 99,
         |            "share": 12,
         |            "contributionrate": 4,
         |            "detail": [ ],
         |            "hospital": [ ],
         |            "competitionproducts": [ ]
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
            "data" -> Json.parse(hospDetail)
        )), None)
    }

}
