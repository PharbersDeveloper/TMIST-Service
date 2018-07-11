package module.proposals.dests

import com.mongodb.casbah.Imports.DBObject
import com.pharbers.bmmessages.CommonModules
import com.pharbers.cliTraits.DBTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
import org.bson.types.ObjectId
import play.api.libs.json.Json.toJson
import com.mongodb.casbah.Imports._
import java.util.UUID.randomUUID

import com.mongodb.casbah.Imports
import com.mongodb.casbah.Imports.{$or, DBObject, MongoDBObject, _}
import com.pharbers.module.DBManagerModule
import module.common.datamodel.cdr
import play.api.libs.json.{JsNumber, JsObject, JsString, JsValue}

case class dest2(dest_id: String, `type`: String, hosp_name: String)

object dest extends App with cdr {

    val conn = new DBManagerModule
    val db: DBTrait = conn.queryDBInstance("client").get

    // dests actions resources goods
    val mjv0: Option[Map[String, JsValue]] = db.queryObject(DBObject(), "test")(dr)

    println(mjv0)




}
