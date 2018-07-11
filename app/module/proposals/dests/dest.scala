package module.proposals.dests

import com.mongodb.casbah.Imports.DBObject
import com.pharbers.bmmessages.CommonModules
import com.pharbers.cliTraits.DBTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
import org.bson.types.ObjectId
import play.api.libs.json.JsValue
import com.mongodb.casbah.Imports._
import play.api.libs.json.Json.toJson

import CaseClassMapConverter._

case class dest2(dest_id: String, `type`: String, hosp_name: String)

class dest(cm: CommonModules) {

    val conn: dbInstanceManager = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
    val db: DBTrait = conn.queryDBInstance("client").get
    val func_out: DBObject => Map[String, JsValue] = { obj =>
        Map(
            "dest_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "type" -> toJson(obj.getAs[String]("type").get),
            "hosp_name" -> toJson(obj.getAs[String]("hosp_name").get)
        )
    }

    def ccToMap[C: CaseClassMapConverter](c: C): Map[String,Any] =
        implicitly[CaseClassMapConverter[C]].toMap(c)

    def mapTocc[C: CaseClassMapConverter](m: Map[String,Any]): C =
        implicitly[CaseClassMapConverter[C]].fromMap(m)

    val mjv = db.queryObject(DBObject(), "dests")(func_out) match {
        case Some(m) => m
        case None => Map().empty
    }
    val a = mjv.map(x =>
        x._1.toString -> x._2.toString
    )

    val civic = dest2("Civic", "2016", "Honda")
    println(ccToMap[dest2](civic))
    println(mapTocc[dest2](a))


//    val cc = mjv2cc.toCC(mjv)

    //    "_id" : ObjectId("5b43118fed925c05565b5bfc"),
    //    "type" : "hosp",
    //    "hosp_name" : "中日医院",
    //    "set_time" : NumberInt(1984),
    //    "category" : "综合",
    //    "hosp_level" : "三级",
    //    "client_grade" : "客户分级",
    //    "beds" : NumberLong(1000),
    //    "department" : "神经内科.感染疾病科.骨科.消化内科.胸外科.妇产科.普通外科",
    //    "features_department" : "神经内科",
    //    "features_outpatient" : "特色门诊",
    //    "receive_academic_degrees" : "学术收纳度",
    //    "academic_influence" : "学术影响力",
    //    "patient_distribution_department" : "病人科室分布",
    //    "outpatient_year" : NumberLong(10000),
    //    "stationierung_year" : NumberLong(1230),
    //    "surgery_year" : NumberLong(800),
    //    "payment_capacity" : "高",
    //    "drug_into" : "进药情况"

    cm

}
