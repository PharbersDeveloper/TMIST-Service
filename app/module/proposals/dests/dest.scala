//package module.proposals.dests
//
//import play.api.libs.json.JsValue
//import module.common.datamodel.cdr
//import com.pharbers.cliTraits.DBTrait
//import com.mongodb.casbah.Imports.DBObject
//import com.pharbers.module.DBManagerModule
//
//case class dest2(dest_id: String, `type`: String, hosp_name: String)
//
//object dest extends App with cdr {
//
//    val conn = new DBManagerModule
//    val db: DBTrait = conn.queryDBInstance("client").get
//
//    // dests actions resources goods
//    val mjv0: Option[Map[String, JsValue]] = db.queryObject(DBObject(), "test")(dr)
//
//    println(mjv0)
//}
