package module.scenarios.update

import play.api.libs.json._
import scala.reflect.ClassTag
import com.mongodb.casbah.Imports._
import com.pharbers.bmmessages.CommonModules
import com.pharbers.dbManagerTrait.dbInstanceManager

class updateScenario extends ClassTag[updateScenario] {
    val name = "scenario"
    val names = "scenarios"

    override def runtimeClass: Class[_] = classOf[updateScenario]

    def update(data: JsValue)
              (func_in: JsValue => DBObject,
               func_update: (DBObject, JsValue) => DBObject,
               func_out: DBObject => Map[String, JsValue])
              (implicit cm: CommonModules): Map[String, JsValue] = {

        val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
        val db = conn.queryDBInstance("client").get

        db.queryObject(func_in(data), names) { obj =>
            val tmp = func_update(obj, data)
            db.updateObject(tmp, names, "_id")
            func_out(tmp)
        } match {
            case Some(m) => m
            case None => Map().empty
        }
    }

    val qc: JsValue => DBObject = { js =>
        val uuid = (js \ "data" \ "condition" \ "uuid").asOpt[String].get
        DBObject("uuid" -> uuid)
    }

    val upDGR: (DBObject, JsValue) => DBObject = { (obj, js) =>

        val in_data = (js \ "data" \ "data").as[JsArray].value

        val current = obj.get("current").asInstanceOf[DBObject]

        in_data.foreach { data =>
            val dest_id = (data \ "dest_id").as[JsString].value
            val goods_id = (data \ "goods_id").as[JsString].value
            val rep_id = (data \ "rep_id").as[JsString].value

            val (targetDGR, keepDGR) = {
                val dgrLst = current.get("dest_goods_rep").asInstanceOf[BasicDBList].toList.map(_.asInstanceOf[DBObject])

                val targetDGR = dgrLst.filter(o => o.get("dest_id").asInstanceOf[String] == dest_id)
                        .filter(o => o.get("goods_id").asInstanceOf[String] == goods_id)
                        .filter(o => o.get("rep_id").asInstanceOf[String] == rep_id)

                val keepDGR = dgrLst diff targetDGR

                (targetDGR.headOption, keepDGR)
            }

            val newTargetDGR = targetDGR match {
                case Some(old) =>
                    val relationship = old.get("relationship").asInstanceOf[DBObject]
                    relationship += "user_input_day" -> int2Integer(data("user_input_day").asInstanceOf[JsNumber].value.toInt)
                    relationship += "budget_proportion" -> double2Double(data("budget_proportion").asInstanceOf[JsNumber].value.toDouble)
                    relationship += "user_input_target" -> long2Long(data("user_input_target").asInstanceOf[JsNumber].value.toLong)
                    relationship += "target_growth" -> double2Double(data("target_growth").asInstanceOf[JsNumber].value.toDouble)
                    relationship += "user_input_money" -> long2Long(data("user_input_money").asInstanceOf[JsNumber].value.toLong)

                    old += "relationship" -> relationship
                    old
                case None =>
                    val builder = MongoDBObject.newBuilder
                    builder += "user_input_day" -> int2Integer(data("user_input_day").asInstanceOf[JsNumber].value.toInt)
                    builder += "budget_proportion" -> double2Double(data("budget_proportion").asInstanceOf[JsNumber].value.toDouble)
                    builder += "user_input_target" -> long2Long(data("user_input_target").asInstanceOf[JsNumber].value.toLong)
                    builder += "target_growth" -> double2Double(data("target_growth").asInstanceOf[JsNumber].value.toDouble)
                    builder += "user_input_money" -> long2Long(data("user_input_money").asInstanceOf[JsNumber].value.toLong)

                    val relationship = builder.result
                    DBObject(
                        "dest_id" -> dest_id,
                        "goods_id" -> goods_id,
                        "rep_id" -> rep_id,
                        "relationship" -> relationship
                    )
            }

            val dgrLst = MongoDBList(newTargetDGR :: keepDGR: _*).underlying

            current += "dest_goods_rep" -> dgrLst
        }

        obj += "current" -> current

        obj
    }

    val c2p: (DBObject, JsValue) => DBObject = { (obj, js) =>
        val in_data = (js \ "data" \ "data").as[JsArray].value

        val current = obj.get("current").asInstanceOf[DBObject]

        in_data.foreach { data =>
            val dest_id = (data \ "dest_id").as[JsString].value
            val goods_id = (data \ "goods_id").as[JsString].value
            val rep_id = (data \ "rep_id").as[JsString].value

            val (targetDGR, keepDGR) = {
                val dgrLst = current.get("dest_goods_rep").asInstanceOf[BasicDBList].toList.map(_.asInstanceOf[DBObject])

                val targetDGR = dgrLst.filter(o => o.get("dest_id").asInstanceOf[String] == dest_id)
                        .filter(o => o.get("goods_id").asInstanceOf[String] == goods_id)
                        .filter(o => o.get("rep_id").asInstanceOf[String] == rep_id)

                val keepDGR = dgrLst diff targetDGR

                (targetDGR.headOption, keepDGR)
            }

            val newTargetDGR = targetDGR match {
                case Some(old) =>
                    val relationship = old.get("relationship").asInstanceOf[DBObject]
                    relationship += "user_input_day" -> int2Integer(data("user_input_day").asInstanceOf[JsNumber].value.toInt)
                    relationship += "budget_proportion" -> double2Double(data("budget_proportion").asInstanceOf[JsNumber].value.toDouble)
                    relationship += "user_input_target" -> long2Long(data("user_input_target").asInstanceOf[JsNumber].value.toLong)
                    relationship += "target_growth" -> double2Double(data("target_growth").asInstanceOf[JsNumber].value.toDouble)
                    relationship += "user_input_money" -> long2Long(data("user_input_money").asInstanceOf[JsNumber].value.toLong)

                    old += "relationship" -> relationship
                    old
                case None =>
                    val builder = MongoDBObject.newBuilder
                    builder += "user_input_day" -> int2Integer(data("user_input_day").asInstanceOf[JsNumber].value.toInt)
                    builder += "budget_proportion" -> double2Double(data("budget_proportion").asInstanceOf[JsNumber].value.toDouble)
                    builder += "user_input_target" -> long2Long(data("user_input_target").asInstanceOf[JsNumber].value.toLong)
                    builder += "target_growth" -> double2Double(data("target_growth").asInstanceOf[JsNumber].value.toDouble)
                    builder += "user_input_money" -> long2Long(data("user_input_money").asInstanceOf[JsNumber].value.toLong)

                    val relationship = builder.result
                    DBObject(
                        "dest_id" -> dest_id,
                        "goods_id" -> goods_id,
                        "rep_id" -> rep_id,
                        "relationship" -> relationship
                    )
            }

            val dgrLst = MongoDBList(newTargetDGR :: keepDGR: _*).underlying

            current += "dest_goods_rep" -> dgrLst
        }

        obj += "current" -> current

        obj
    }

}