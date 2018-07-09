package module.proposals.reports

import module.roles.role
import module.common.processor
import play.api.libs.json.Json.toJson
import com.pharbers.bmpattern.ModuleTrait
import play.api.libs.json.{JsValue, Json}
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import module.proposals.ProposalMessage._

object ReportModule extends ModuleTrait {
    val role = new role()

    val name = "checkppoint"
    val names = "checkppoints"

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])
                   (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_getTotalReport(data) =>
            processor(value => getTotalReport(value))(data)
        case msg_getHospProdReport(data) =>
            processor(value => getHospProdReport(value))(data)
        case msg_getRepProdReport(data) =>
            processor(value => getRepProdReport(value))(data)
        case msg_getResourceIO(data) =>
            processor(value => getResourceIO(value))(data)
        case msg_getRepIndResources(data) =>
            processor(value => getRepIndResources(value))(data)

        case _ => ???
    }

    def getTotalReport(data: JsValue): (Option[String Map JsValue], Option[JsValue]) = {
        val hospitalList =
            """
              |      {
              |    "type": "whole_report",
              |    "attribute": {
              |    "total_report": {
              |        "overview": [
                      |        {
                      |            "index": 0,
                      |            "title": "总销售额",
                      |            "type": "sales",
                      |            "value": 6039535
                      |        },
                      |        {
                      |            "index": 1,
                      |            "title": "整体销售增长",
                      |            "type": "percent",
                      |            "value": 12,
                      |            "ext": {
                      |                "change": "up"
                      |            }
                      |        },
                      |        {
                      |            "index": 2,
                      |            "title": "平均指标达成",
                      |            "type": "percent",
                      |            "value": 123,
                      |            "ext": {
                      |                "change": "none"
                      |            }
                      |        }
                      |    ],
              |        "value": [
              |            {
              |                "prod": "产品一",
              |                "market_sale": 123456,
              |                "market_growth": 12,
              |                "current_sales": 45175,
              |                "sales_growth": 16,
              |                "ev_value": 100,
              |                "share": 45,
              |                "share_growth": 9,
              |                "target": 5861,
              |                "achievement_rate": 47,
              |                "contribution_rate": 41
              |            },{
              |                "prod": "产品二",
              |                "market_sale": 123456,
              |                "market_growth": 12,
              |                "current_sales": 45175,
              |                "sales_growth": 16,
              |                "ev_value": 100,
              |                "share": 45,
              |                "share_growth": 9,
              |                "target": 5861,
              |                "achievement_rate": 47,
              |                "contribution_rate": 41
              |            },{
              |                "prod": "产品三",
              |                "market_sale": 123456,
              |                "market_growth": 12,
              |                "current_sales": 45175,
              |                "sales_growth": 16,
              |                "ev_value": 100,
              |                "share": 45,
              |                "share_growth": 9,
              |                "target": 5861,
              |                "achievement_rate": 47,
              |                "contribution_rate": 41
              |            }
              |        ]
              |    }
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

    def getHospProdReport(data: JsValue): (Option[String Map JsValue], Option[JsValue]) = {
        val budgetInfo =
            """
              |{
              |    "type": "whole_report",
              |    "attribute": {
              |"hosp_prod_report" : {
              |    "overview": [
              |        {
              |            "index": 0,
              |            "title": "表现最佳",
              |            "type": "info",
              |            "value": "医院1-产品1",
              |            "ext": {
              |                "sub": {
              |                    "title": "份额",
              |                    "type": "percent",
              |                    "value": 12,
              |                    "ext": {
              |                        "type": "percent",
              |                        "change": "up",
              |                        "value": 7
              |                    }
              |                }
              |            }
              |        },
              |        {
              |            "index": 1,
              |            "title": "表现最佳",
              |            "type": "info",
              |            "value": "医院4-产品2",
              |            "ext": {
              |                "sub": {
              |                    "title": "份额",
              |                    "type": "percent",
              |                    "value": 23,
              |                    "ext": {
              |                        "type": "percent",
              |                        "change": "down",
              |                        "value": 9
              |                    }
              |                }
              |            }
              |        },
              |        {
              |            "index": 2,
              |            "title": "销售增长最快",
              |            "type": "info",
              |            "value": "医院2-产品3",
              |            "ext": {
              |                "sub": {
              |                    "type": "title",
              |                    "value": 60359452,
              |                    "ext": {
              |                        "type": "percent",
              |                        "change": "up",
              |                        "value": 19
              |                    }
              |                }
              |            }
              |        },
              |        {
              |            "index": 3,
              |            "title": "贡献度最高",
              |            "type": "info",
              |            "value": "医院4-产品2",
              |            "ext": {
              |                "sub": {
              |                    "type": "title",
              |                    "value": 60359452,
              |                    "ext": {
              |                        "type": "percent",
              |                        "change": "none",
              |                        "value": 9
              |                    }
              |                }
              |            }
              |        },
              |        {
              |            "index": 4,
              |            "title": "平均指标达成",
              |            "type": "percent",
              |            "value": 123,
              |            "ext": {
              |                "change": "none"
              |            }
              |        }
              |    ],
              |     "value": [{
              |			"hospital": "hospital name",
              |			"prod": "prod 01",
              |			"market_potential": 12456,
              |			"market_growth": 12,
              |			"current_sales": 8654,
              |			"sales_growth": 53,
              |			"ev_value": 15,
              |			"share": 56,
              |			"share_growth": 32,
              |			"target": 56865,
              |			"achievement_rate": 35,
              |			"contribution_rate": 57
              |		},{
              |			"hospital": "hospital name",
              |			"prod": "prod 01",
              |			"market_potential": 12456,
              |			"market_growth": 12,
              |			"current_sales": 8654,
              |			"sales_growth": 53,
              |			"ev_value": 15,
              |			"share": 56,
              |			"share_growth": 32,
              |			"target": 56865,
              |			"achievement_rate": 35,
              |			"contribution_rate": 57
              |		},{
              |			"hospital": "hospital name",
              |			"prod": "prod 01",
              |			"market_potential": 12456,
              |			"market_growth": 12,
              |			"current_sales": 8654,
              |			"sales_growth": 53,
              |			"ev_value": 15,
              |			"share": 56,
              |			"share_growth": 32,
              |			"target": 56865,
              |			"achievement_rate": 35,
              |			"contribution_rate": 57
              |		}
              |  ]
              |    }
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
            "data" -> Json.parse(budgetInfo)
        )), None)
    }

    def getRepProdReport(data: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {

        val humansInfo =
            """
              |      {
              |            "type": "humans_progress",
              |            "attribute": {
              |"rep_prod_report": {
              |    "overview": [
              |        {
              |            "index": 0,
              |            "title": "销售增长最快",
              |            "type": "info",
              |            "value": "代表1-产品1",
              |            "ext": {
              |                "sub": {
              |                    "type": "title",
              |                    "value": 6035147,
              |                    "ext": {
              |                        "type": "percent",
              |                        "change": "up",
              |                        "value": 13
              |                    }
              |                }
              |            }
              |        },
              |        {
              |            "index": 1,
              |            "title": "销售增长最快",
              |            "type": "info",
              |            "value": "代表2-产品4",
              |            "ext": {
              |                "sub": {
              |                    "type": "title",
              |                    "value": 6035147,
              |                    "ext": {
              |                        "type": "percent",
              |                        "change": "none",
              |                        "value": 23
              |                    }
              |                }
              |            }
              |        },
              |        {
              |            "index": 2,
              |            "title": "代表平均指标达成",
              |            "type": "percent",
              |            "value": 123.5,
              |            "ext": {
              |                "change": "none"
              |            }
              |        }
              |    ],
              |    "value":  [
              |    {
              |		"rep": "repre name",
              |		"prod": "prod 01",
              |		"current_sales": 8654,
              |		"sales_growth": 53,
              |		"target": 56865,
              |		"achievement_rate": 35,
              |		"contribution_rate": 57
              |	},{
              |		"rep": "repre name",
              |		"prod": "prod 01",
              |		"current_sales": 8654,
              |		"sales_growth": 53,
              |		"target": 56865,
              |		"achievement_rate": 35,
              |		"contribution_rate": 57
              |	},{
              |		"rep": "repre name",
              |		"prod": "prod 01",
              |		"current_sales": 8654,
              |		"sales_growth": 53,
              |		"target": 56865,
              |		"achievement_rate": 35,
              |		"contribution_rate": 57
              |	}
              | ]
              |}
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
            "data" -> Json.parse(humansInfo)
        )), None)
    }

    def getResourceIO(data: JsValue): (Option[String Map JsValue], Option[JsValue]) = {
        val budgetInfo =
            """
              |{
              |    "type": "budget_progress",
              |    "attribute": {

              |    "res_io": {
              |        "overview": [
 |        {
 |            "index": 0,
 |            "title": "预算投入最多",
 |            "type": "info",
 |            "value": "医院1-产品1",
 |            "ext": {
 |                "sub": {
 |                    "type": "title",
 |                    "value": "预算占比",
 |                    "ext": {
 |                        "type": "percent",
 |                        "change": "none",
 |                        "value": 40
 |                    }
 |                }
 |            }
 |        },
 |        {
 |            "index": 1,
 |            "title": "时间投入最多",
 |            "type": "info",
 |            "value": "医院2-产品3",
 |            "ext": {
 |                "sub": {
 |                    "type": "title",
 |                    "value": "小宋",
 |                    "ext": {
 |                        "type": "percent",
 |                        "change": "none",
 |                        "value": 12
 |                    }
 |                }
 |            }
 |        },
 |        {
 |            "index": 2,
 |            "title": "表现最佳",
 |            "type": "info",
 |            "value": "医院2-产品3",
 |            "ext": {
 |                "sub": {
 |                    "title": "份额",
 |                    "type": "percent",
 |                    "value": 34,
 |                    "ext": {
 |                        "type": "percent",
 |                        "change": "up",
 |                        "value": 12
 |                    }
 |                }
 |            }
 |        },
 |        {
 |            "index": 3,
 |            "title": "贡献度最高",
 |            "type": "info",
 |            "value": "医院2-产品3",
 |            "ext": {
 |                "sub": {
 |                    "type": "title",
 |                    "value": 343434,
 |                    "ext": {
 |                        "type": "percent",
 |                        "change": "none",
 |                        "value": 15
 |                    }
 |                }
 |            }
 |        }
 |    ],
              |        "value": [
              |            {
              |                "hospital": "hospital name",
              |                "prod": "prod 01",
              |                "rep": "laowang",
              |                "time": 12,
              |                "budget": 857654,
              |                "market_potential": 12456,
              |                "potential_growth": 12,
              |                "current_sales": 8654,
              |                "sales_growth": 53,
              |                "share": 56,
              |                "share_growth": 32,
              |                "contribution_rate": 57
              |            },{
              |                "hospital": "hospital name",
              |                "prod": "prod 01",
              |                "rep": "laowang",
              |                "time": 12,
              |                "budget": 857654,
              |                "market_potential": 12456,
              |                "potential_growth": 12,
              |                "current_sales": 8654,
              |                "sales_growth": 53,
              |                "share": 56,
              |                "share_growth": 32,
              |                "contribution_rate": 57
              |            },{
              |                "hospital": "hospital name",
              |                "prod": "prod 01",
              |                "rep": "laowang",
              |                "time": 12,
              |                "budget": 857654,
              |                "market_potential": 12456,
              |                "potential_growth": 12,
              |                "current_sales": 8654,
              |                "sales_growth": 53,
              |                "share": 56,
              |                "share_growth": 32,
              |                "contribution_rate": 57
              |            }
              |        ]
              |    }
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
            "data" -> Json.parse(budgetInfo)
        )), None)
    }

    def getRepIndResources(data: JsValue): (Option[String Map JsValue], Option[JsValue]) = {
        val budgetInfo =
            """
              |{
              |    "type": "budget_progress",
              |    "attribute": {
              |    "rep_ind_resources": {
              |        "overview": [
              |        {
              |            "index": 0,
              |            "title": "指标最重",
              |            "type": "info",
              |            "value": "代表1",
              |            "ext": {
              |                "sub": {
              |                    "type": "title",
              |                    "value": "指标",
              |                    "ext": {
              |                        "type": "sales",
              |                        "value": 404040
              |                    }
              |                }
              |            }
              |        },
              |        {
              |            "index": 1,
              |            "title": "资源最多",
              |            "type": "info",
              |            "value": "代表2",
              |            "ext": {
              |                "sub": {
              |                    "type": "title",
              |                    "value": "预算占比",
              |                    "ext": {
              |                        "type": "percent",
              |                        "change": "none",
              |                        "value": 12
              |                    }
              |                }
              |            }
              |        },
              |        {
              |            "index": 2,
              |            "title": "达成率最高",
              |            "type": "info",
              |            "value": "代表3",
              |            "ext": {
              |                "sub": {
              |                    "type": "title",
              |                    "value": "达成率",
              |                    "ext": {
              |                        "type": "percent",
              |                        "change": "none",
              |                        "value": 121
              |                    }
              |                }
              |            }
              |        },
              |        {
              |            "index": 3,
              |            "title": "贡献度最高",
              |            "type": "info",
              |            "value": "代表4",
              |            "ext": {
              |                "sub": {
              |                    "type": "title",
              |                    "value": "贡献度",
              |                    "ext": {
              |                        "type": "percent",
              |                        "change": "none",
              |                        "value": 3
              |                    }
              |                }
              |            }
              |        }
              |    ],
              |        "value": [
              |            {
              |                "rep": "rep",
              |                "target": 5735,
              |                "current_sales": 8654,
              |                "indicator_achievement": 53,
              |                "target_rate": 15,
              |                "contribution_rate": 57,
              |                "workdays": 56,
              |                "reward": 32,
              |                "ability": 56865,
              |                "ability_change": "+3"
              |            }
              |        ]
              |    }
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
            "data" -> Json.parse(budgetInfo)
        )), None)
    }

}
