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
        val user_id = (js \ "user" \ "user_id").asOpt[String].get
        val proposal_id = (js \ "data" \ "condition" \ "proposal_id").asOpt[String].get
        DBObject("user_id" -> user_id, "proposal_id" -> proposal_id)
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


    //    override val popr : DBObject => Map[String, JsValue] = { _ =>
    //        Map(
    //            "pop proposal" -> toJson("success")
    //        )
    //    }
    //
    //    override val d2m : JsValue => DBObject = { js =>
    //        val data = (js \ "data" \ "proposal").asOpt[JsValue].map (x => x).getOrElse(toJson(""))
    //
    //        val builder = MongoDBObject.newBuilder
    //        builder += "_id" -> ObjectId.get()      // proposal_id 唯一标示
    //        builder += "proposal_name" -> (data \ "proposal_name").asOpt[String].map (x => x).getOrElse("")
    //        builder += "proposal_des" -> (data \ "proposal_des").asOpt[String].map (x => x).getOrElse("")
    //
    //        builder.result
    //    }
    //
}
