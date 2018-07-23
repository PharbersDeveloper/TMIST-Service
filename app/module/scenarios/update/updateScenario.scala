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

        val in_cond = (js \ "data" \ "condition").asOpt[JsValue].get.as[JsObject].value - "uuid"

        val current = obj.get("current").asInstanceOf[DBObject]

        val (targetDGR, keepDGR) ={
            val dgrLst = current.get("dest_goods_rep").asInstanceOf[BasicDBList].toList.map(_.asInstanceOf[DBObject])

            val targetDGR = dgrLst.filter(o => o.get("dest_id").asInstanceOf[String] == in_cond("dest_id").asInstanceOf[JsString].value)
                    .filter(o => o.get("goods_id").asInstanceOf[String] == in_cond("goods_id").asInstanceOf[JsString].value)
                    .filter(o => o.get("rep_id").asInstanceOf[String] == in_cond("rep_id").asInstanceOf[JsString].value)

            val keepDGR = dgrLst diff targetDGR

            (targetDGR.head, keepDGR)
        }

        val relationship = {
            val obj = targetDGR.get("relationship").asInstanceOf[DBObject]
            val tmp = (js \ "data" \ "dgr").as[JsObject].value
            obj += "user_input_day" -> int2Integer(tmp("user_input_day").asInstanceOf[JsNumber].value.toInt)
            obj += "budget_proportion" -> double2Double(tmp("budget_proportion").asInstanceOf[JsNumber].value.toDouble)
            obj += "user_input_target" -> long2Long(tmp("user_input_target").asInstanceOf[JsNumber].value.toLong)
            obj += "target_growth" -> double2Double(tmp("target_growth").asInstanceOf[JsNumber].value.toDouble)
            obj += "user_input_money" -> long2Long(tmp("user_input_money").asInstanceOf[JsNumber].value.toLong)

            obj
        }

        targetDGR += "relationship" -> relationship
        val dgrLst = MongoDBList(targetDGR :: keepDGR: _*).underlying
        current += "dest_goods_rep" -> dgrLst
        obj += "current" -> current

        obj
    }

}