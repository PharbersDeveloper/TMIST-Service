package module.proposals.scenarios

import module.common.processor._
import module.proposals.{proposal, proposal2scenario}
import module.common.stragety.impl
import play.api.libs.json.Json.toJson
import module.proposals.ProposalMessage._
import com.pharbers.bmpattern.ModuleTrait
import play.api.libs.json.{JsValue, Json}
import module.common.processor.returnValue
import module.common.{MergeStepResult, processor}
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import module.proposals.scenarios.products.{hospital2product, product}
import module.proposals.scenarios.hospitals.{hospital, scenario2hospital}
import module.proposals.scenarios.representatives.{hospital2representative, representative}

object ScenarioModule extends ModuleTrait {
    val p: proposal = impl[proposal]
    val s: scenario = impl[scenario]
    val h: hospital = impl[hospital]
    val r: representative = impl[representative]
    val prod: product = impl[product]
    val ps: proposal2scenario = impl[proposal2scenario]
    val sh: scenario2hospital = impl[scenario2hospital]
    val hr: hospital2representative = impl[hospital2representative]
    val hp: hospital2product = impl[hospital2product]

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])
                   (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_queryScenariosByProposal(data) =>
            processor(value => returnValue(ps.queryConnection(value)(pr)(s.sr)("bind_proposal_scenario")))(MergeStepResult(data, pr))
        case msg_getFirstScenario(data) =>
            processor (_ => (Some(Map(s.name -> pr.get(p.name).asOpt[Map[String, JsValue]].get(s.names).asOpt[List[JsValue]].get.head)), None))(data)

        case _ => ???
    }

    def getHospitalList(data: JsValue): (Option[String Map JsValue], Option[JsValue]) = {

        val hospitalList =
            """
              |      {
              |    "type": "hosp_lst",
              |    "result": {
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

        (Some(Map(
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
