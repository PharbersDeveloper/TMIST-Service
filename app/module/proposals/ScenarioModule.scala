package module.proposals

import module.roles.role
import module.common.processor
import module.common.processor._
import play.api.libs.json.Json.toJson
import module.proposals.ProposalMessage._
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

        case msg_getHospLst(data) =>
            processor(value => getHospitalList(value))(data)
        //            pushMacro(d2m, ssr, data, names, name)
        case msg_getBudgetInfo(data) =>
            processor(value => getBudgetInfo(value))(data)
        //            popMacro(qc, popr, data, names)
        case msg_getHumansInfo(data) =>
            //updateMacro(qc, up2m, dr, data, names, name)
            processor(value => getHumansInfo(value))(data)
        case msg_getHospDetail(data) =>
            //queryMultiMacro(???, ???, MergeStepResult(data, pr), names, names)
            processor(value => getHospDetail(value))(data)

        case _ => ???
    }

    def getHospitalList(data: JsValue): (Option[String Map JsValue], Option[JsValue]) = {
        val hospitalList =
            """
              |      {
              |    "type": "hosp_lst",
              |    "attribute": {
              |    "currentMonth": "2",
              |    "hospitalList": [
              |        {
              |            "hospid": "111",
              |            "name": "中日医院",
              |            "level": "综合/三甲",
              |            "department": "皮肤科",
              |            "beds": 1000,
              |            "outpatient": 1234545,
              |            "surgery": 1000,
              |            "representive": {
              |                "name": "校长",
              |                "avatar": "/assets/images/hosp_seller.png"
              |            },
              |            "medicine": [
              |                {
              |                    "name": "口服抗生素",
              |                    "potential": 54561334,
              |                    "previoussales": 554687,
              |                    "contributionrate": 5,
              |                    "share": 12
              |                },{
              |                    "name": "口服护发素",
              |                    "potential": 67561334,
              |                    "previoussales": 654687,
              |                    "contributionrate": 54,
              |                    "share": 12
              |                },{
              |                    "name": "口服叶绿素",
              |                    "potential": 94561334,
              |                    "previoussales": 954687,
              |                    "contributionrate": 65,
              |                    "share": 230
              |                }
              |            ]
              |        },{
              |            "hospid": "222",
              |            "name": "小鬼子医院",
              |            "level": "综合/三丙",
              |            "department": "地雷科",
              |            "beds": 1000,
              |            "outpatient": 1234545,
              |            "surgery": 1000,
              |            "representive": {
              |                "name": "校长",
              |                "avatar": "/assets/images/hosp_seller.png"
              |            },
              |            "medicine": [
              |                {
              |                    "name": "口服抗生素",
              |                    "potential": 54561334,
              |                    "previoussales": 554687,
              |                    "contributionrate": 5,
              |                    "share": 12
              |                },{
              |                    "name": "口服护发素",
              |                    "potential": 67561334,
              |                    "previoussales": 654687,
              |                    "contributionrate": 54,
              |                    "share": 12
              |                },{
              |                    "name": "口服叶绿素",
              |                    "potential": 94561334,
              |                    "previoussales": 954687,
              |                    "contributionrate": 65,
              |                    "share": 230
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
            "data" -> Json.parse(hospitalList)
        )), None)
    }

    def getBudgetInfo(data: JsValue): (Option[String Map JsValue], Option[JsValue]) = {
        val budgetInfo =
            """
              |{
              |    "type": "budget_progress",
              |    "attribute": {
              |        "total": 800000,
              |        "used": 60000
              |    }
              |}
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
            "data" -> Json.parse(budgetInfo)
        )), None)
    }

    def getHumansInfo(data: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {

        val humansInfo =
            """
              |      {
              |            "type": "humans_progress",
              |            "attribute": [
              |    {
              |        "name": "代表一",
              |        "total": 30,
              |        "used": 20
              |    },
              |    {
              |        "name": "代表二",
              |        "total": 30,
              |        "used": 29
              |    },
              |    {
              |        "name": "代表三",
              |        "total": 30,
              |        "used": 20
              |    },
              |    {
              |        "name": "代表四",
              |        "total": 30,
              |        "used": 15
              |    },
              |    {
              |        "name": "代表五",
              |        "total": 30,
              |        "used": 10
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

    def getHospDetail(data: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {

        val hospDetail =
            """
              |{
              |"type": "humans_progress",
              |"attribute":
              |{
              |    "hospital": {
              |        "id": "idididi",
              |        "name": "中日医院",
              |        "basicinfo": {
              |            "type": "综合",
              |            "level": "综合/三甲",
              |            "department": "皮肤科",
              |            "beds": "1000",
              |            "outpatient": 1234545,
              |            "surgery": 1000,
              |            "hospitalizations": 1000
              |        },
              |        "news": { },
              |        "policy": { }
              |    },
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
              |            "detail": {
              |                "id": "medicine000_detail",
              |                "value": [
              |                    {
              |                        "id": "霉素",
              |                        "type": "口服抗生素",
              |                        "treatmentarea": "抗生素",
              |                        "selltime": "2000",
              |                        "medicalinsurance": "甲类",
              |                        "development": "首仿",
              |                        "companyprice": 44
              |                    }
              |                ]
              |            },
              |            "history": {
              |                "id": "medicine000_history",
              |                "value": [
              |                    {
              |                        "time": "星期一",
              |                        "representative": "小三",
              |                        "timemanagement": "12天",
              |                        "budgetallocation": 5000,
              |                        "budgetratio": 5000,
              |                        "indicator": 50000,
              |                        "growth": 1.23,
              |                        "achievementrate": 95
              |                    }
              |                ]
              |            },
              |            "competitionproducts": {
              |                "id": "medicine000_competitionproducts",
              |                "value": [ ]
              |            }
              |        },{
              |            "id": "medicine001",
              |            "name": "口服护发素",
              |            "marketpotential": 333444555,
              |            "potentialgrowth": 99,
              |            "previoussales": 555444,
              |            "previousgrowth": 99,
              |            "share": 12,
              |            "contributionrate": 4,
              |            "detail": {
              |                "id": "medicine001_detail",
              |                "value": [
              |                    {
              |                        "id": "霉素",
              |                        "type": "口服抗生素",
              |                        "treatmentarea": "抗生素",
              |                        "selltime": "2000",
              |                        "medicalinsurance": "甲类",
              |                        "development": "首仿",
              |                        "companyprice": 44
              |                    }
              |                ]
              |            },
              |            "history": {
              |                "id": "medicine001_history",
              |                "value": [
              |                    {
              |                        "time": "星期一",
              |                        "representative": "小三",
              |                        "timemanagement": "12天",
              |                        "budgetallocation": 5000,
              |                        "budgetratio": 5000,
              |                        "indicator": 50000,
              |                        "growth": 1.23,
              |                        "achievementrate": 95
              |                    }
              |                ]
              |            },
              |            "competitionproducts": {
              |                "id": "medicine001_competitionproducts",
              |                "value": [ ]
              |            }
              |        },{
              |            "id": "medicine002",
              |            "name": "口服叶绿素",
              |            "marketpotential": 333444555,
              |            "potentialgrowth": 99,
              |            "previoussales": 555444,
              |            "previousgrowth": 99,
              |            "share": 12,
              |            "contributionrate": 4,
              |            "detail": {
              |                "id": "medicine002_detail",
              |                "value": [
              |                    {
              |                        "id": "霉素",
              |                        "type": "口服抗生素",
              |                        "treatmentarea": "抗生素",
              |                        "selltime": "2000",
              |                        "medicalinsurance": "甲类",
              |                        "development": "首仿",
              |                        "companyprice": 44
              |                    }
              |                ]
              |            },
              |            "history": {
              |                "id": "medicine002_history",
              |                "value": [
              |                    {
              |                        "time": "星期一",
              |                        "representative": "小三",
              |                        "timemanagement": "12天",
              |                        "budgetallocation": 5000,
              |                        "budgetratio": 5000,
              |                        "indicator": 50000,
              |                        "growth": 1.23,
              |                        "achievementrate": 95
              |                    }
              |                ]
              |            },
              |            "competitionproducts": {
              |                "id": "medicine002_competitionproducts",
              |                "value": [ ]
              |            }
              |        }
              |    ]
              |}
              |}
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
