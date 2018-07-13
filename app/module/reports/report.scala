package module.reports

import com.mongodb.casbah.Imports
import play.api.libs.json.JsValue
import com.mongodb.casbah.Imports._
import module.common.datamodel.cdr
import com.pharbers.bmmessages.CommonModules
import com.pharbers.dbManagerTrait.dbInstanceManager

import scala.reflect.ClassTag

/**
  * Created by clock on 18-7-13.
  */
class report extends ClassTag[report] with cdr {

    val name = "report"
    val names = "reports"

    override def runtimeClass: Class[_] = classOf[report]

    implicit val idr: Imports.DBObject => Map[String, JsValue] = dr
    implicit val qc: JsValue => DBObject = { js =>
        val report_id = (js \ "data" \ "condition" \ "report_id").asOpt[String].get
        DBObject("_id" -> new ObjectId(report_id))
    }

    def query(data : JsValue)(db_name : String)
             (implicit func : JsValue => DBObject,
              func_out : DBObject => Map[String, JsValue],
              cm: CommonModules) : Map[String, JsValue] = {

        val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
        val db = conn.queryDBInstance("report").get
        db.queryObject(func(data), db_name)(func_out) match {
            case Some(m) => m
            case None => Map().empty
        }
    }

}
