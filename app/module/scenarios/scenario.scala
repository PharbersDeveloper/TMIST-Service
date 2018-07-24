package module.scenarios

import scala.reflect.ClassTag
import com.mongodb.casbah.Imports._
import module.common.datamodel.cdr
import com.pharbers.cliTraits.DBTrait
import play.api.libs.json.Json.toJson
import com.pharbers.bmmessages.CommonModules
import com.pharbers.dbManagerTrait.dbInstanceManager
import play.api.libs.json.{JsArray, JsObject, JsString, JsValue}

/**
  * Created by clock on 18-7-11.
  */
class scenario extends ClassTag[scenario] with cdr {

    val name = "scenario"
    val names = "scenarios"

    override def runtimeClass: Class[_] = classOf[scenario]

    val qc: JsValue => DBObject = { js =>
        val uuid = (js \ "data" \ "condition" \ "uuid").asOpt[String].get
        DBObject("uuid" -> uuid)
    }

    val qcm : JsValue => DBObject = { js =>
        val user_id = (js \ "user" \ "user_id").asOpt[String].get
        (js \ "user" \ "proposals").as[JsArray].value.toList
                .map(_.asInstanceOf[JsObject].value) match {
            case Nil => DBObject("query" -> "none")
            case ll => $or (ll map (x =>
                DBObject(
                    "user_id" -> user_id,
                    "proposal_id" -> x("proposal_id").as[JsString].value)
                )
            )
        }
    }

    val ssr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "user_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "proposal_id" -> toJson(obj.getAs[String]("proposal_id").get.toString),
            "uuid" -> toJson(obj.getAs[String]("uuid").get.toString)
        )
    }

    def queryConnectData(pr: Option[Map[String, JsValue]])
                        (key: String)
                        (coll_name: String)
                        (out: DBObject => Map[String, JsValue])
                        (implicit cm: CommonModules): Map[String, JsValue] = {

        val conn: dbInstanceManager = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
        val db: DBTrait = conn.queryDBInstance("client").get

        val root = pr.get(name).asInstanceOf[JsObject].value
        val current = root("current").asInstanceOf[JsObject].value
        val connect = current(key).asInstanceOf[JsArray].value

        val connectLst = connect.toList
        val currentTmp = connectLst.map{ conn =>
            val conn_obj = conn.asInstanceOf[JsObject].value
            val conn_cond = DBObject("_id" -> new ObjectId(conn_obj("id").asInstanceOf[JsString].value))

            conn_obj ++ db.queryObject(conn_cond, coll_name)(out).get
        }


        val postLst = root("past").asInstanceOf[JsArray].value
        val postTmp = postLst.map{ post =>
            val connect = post(key).asInstanceOf[JsArray].value

            val connectLst = connect.toList
            val tmp = connectLst.map { conn =>
                val conn_obj = conn.asInstanceOf[JsObject].value
                val conn_cond = DBObject("_id" -> new ObjectId(conn_obj("id").asInstanceOf[JsString].value))

                conn_obj ++ db.queryObject(conn_cond, coll_name)(out).get
            }

            toJson(post.asInstanceOf[JsObject].value ++ Map(key -> toJson(tmp)))
        }

        Map(
            name -> toJson(
                root ++ Map(
                    "current" -> toJson(
                        current ++ Map(
                            key -> toJson(currentTmp)
                        )
                    ),
                    "past" -> toJson(postTmp)
                )
            )
        )
    }

}
