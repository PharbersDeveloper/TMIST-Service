//package module.proposals.dests
//
//import com.mongodb.casbah.Imports.DBObject
//import com.pharbers.bmmessages.CommonModules
//import com.pharbers.cliTraits.DBTrait
//import com.pharbers.dbManagerTrait.dbInstanceManager
//import org.bson.types.ObjectId
//import play.api.libs.json.Json.toJson
//import com.mongodb.casbah.Imports._
//import java.util.UUID.randomUUID
//
//import com.mongodb.casbah.Imports
//import com.mongodb.casbah.Imports.{$or, DBObject, MongoDBObject, _}
//import module.scenarios.entity.action.entity
//import module.scenarios.entity.{med, rep}
//import play.api.libs.json.{JsNumber, JsObject, JsString, JsValue}
//
//case class dest2(dest_id: String, `type`: String, hosp_name: String)
//
//class dest(cm: CommonModules) {
//
//    val conn: dbInstanceManager = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
//    val db: DBTrait = conn.queryDBInstance("client").get
//
//    // dests actions resources goods
//    val mjv0: Option[Map[String, JsValue]] = db.queryObject(DBObject(), "actions")(entity.d2m)
//
//    val mjv = mjv0.get("current").asInstanceOf[JsObject].value
//
//    val connectDestLst = mjv("connect_dest").asInstanceOf[JsObject].value.values.toList
//    val destCond = $or(connectDestLst map (x => DBObject("_id" -> new ObjectId(x("id").asOpt[JsString].get.value))))
//    val destLst = db.queryMultipleObject(destCond, "dests")(entity.d2m)
//
//    val connectResoLst = mjv("connect_reso").asInstanceOf[JsObject].value.values.toList
//    val resoCond = $or(connectResoLst map (x => DBObject("_id" -> new ObjectId(x("id").asOpt[JsString].get.value))))
//    val resoLst = db.queryMultipleObject(resoCond, "resources")(entity.d2m)
//
//    val connectGoodsLst = mjv("connect_goods").asInstanceOf[JsObject].value.values.toList
//    val goodsCond = $or(connectGoodsLst map (x => DBObject("_id" -> new ObjectId(x("id").asOpt[JsString].get.value))))
//    val goodsLst = db.queryMultipleObject(goodsCond, "goods")(entity.d2m)
//
//
//    destLst.foreach(println)
//    resoLst.foreach(println)
//    goodsLst.foreach(println)
//
////    Map(
////        "action_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
////        "uuid" -> toJson(obj.getAs[String]("uuid").get),
////        "user_id" -> toJson(obj.getAs[String]("user_id").get),
////        "proposal_id" -> toJson(obj.getAs[String]("proposal_id").get),
////        "timestape" -> toJson(obj.getAs[Long]("timestape").get),
////        "current" -> toJson(obj.getAs[DBObject]("current").get.toString),
////        "post" -> toJson(obj.getAs[List[DBObject]]("post").get.toString)
////    )
//
////    mjv.foreach(x =>
////        if (x._2.isInstanceOf[JsString] || x._2.isInstanceOf[JsNumber])
////            println(x._1 + " -> " + x._2)
////        else
////            println("wocao" + x._1 + " -> " + x._2)
////    )
//
//}
