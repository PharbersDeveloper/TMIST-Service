package module.proposals.dests

import com.mongodb.casbah.Imports.DBObject
import com.pharbers.bmmessages.CommonModules
import com.pharbers.cliTraits.DBTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
import org.bson.types.ObjectId
import play.api.libs.json.Json.toJson
import com.mongodb.casbah.Imports._
import java.util.UUID.randomUUID

import play.api.libs.json.JsValue

case class dest2(dest_id: String, `type`: String, hosp_name: String)

class dest(cm: CommonModules) {

    val conn: dbInstanceManager = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
    val db: DBTrait = conn.queryDBInstance("client").get

    val mjv = db.queryObject(DBObject(), "actions") { obj =>
        println(obj)
        Map(
            "action_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "uuid" -> toJson(obj.getAs[String]("uuid").get),
            "user_id" -> toJson(obj.getAs[String]("user_id").get),
            "proposal_id" -> toJson(obj.getAs[String]("proposal_id").get),
            "timestape" -> toJson(obj.getAs[Long]("timestape").get),
            "current" -> toJson(obj.getAs[DBObject]("current").get.toString),
            "post" -> toJson(obj.getAs[List[DBObject]]("post").get.toString)
        )

    }

    mjv.get.foreach(x =>
        println(x._1 + " -> " + x._2)
    )

}
