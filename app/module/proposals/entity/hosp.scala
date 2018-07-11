package module.proposals.entity

import org.bson.types.ObjectId
import play.api.libs.json.JsValue
import com.mongodb.casbah.Imports._
import play.api.libs.json.Json.toJson
import com.mongodb.casbah.Imports.DBObject

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
//
//class hosp(dest_id: String = "", `type`: String = "", hosp_name: String = "",
//           set_time: Int = 0, category: String = "", hosp_level: String = "",
//           client_grade: String = "", beds: Long = 0, department: String = "",
//           features_department: String = "", features_outpatient: String = "", receive_academic_degrees: String = "",
//           academic_influence: String = "", patient_distribution_department: String = "", outpatient_year: Long = 0,
//           stationierung_year: Long = 0, surgery_year: Long = 0, payment_capacity: String = "", drug_into: String = "")

object hosp {
    val dr: DBObject => Map[String, JsValue] = { obj =>
        Map(
            "dest_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "type" -> toJson(obj.getAs[String]("type").get),
            "hosp_name" -> toJson(obj.getAs[String]("hosp_name").get),

            "set_time" -> toJson(obj.getAs[Int]("set_time").get),
            "category" -> toJson(obj.getAs[String]("category").get),
            "hosp_level" -> toJson(obj.getAs[String]("hosp_level").get),

            "client_grade" -> toJson(obj.getAs[String]("client_grade").get),
            "beds" -> toJson(obj.getAs[Long]("beds").get),
            "department" -> toJson(obj.getAs[String]("department").get),

            "features_department" -> toJson(obj.getAs[String]("features_department").get),
            "features_outpatient" -> toJson(obj.getAs[String]("features_outpatient").get),
            "receive_academic_degrees" -> toJson(obj.getAs[String]("receive_academic_degrees").get),

            "academic_influence" -> toJson(obj.getAs[String]("academic_influence").get),
            "patient_distribution_department" -> toJson(obj.getAs[String]("patient_distribution_department").get),
            "outpatient_year" -> toJson(obj.getAs[Long]("outpatient_year").get),

            "stationierung_year" -> toJson(obj.getAs[Long]("stationierung_year").get),
            "surgery_year" -> toJson(obj.getAs[Long]("surgery_year").get),
            "payment_capacity" -> toJson(obj.getAs[String]("payment_capacity").get),
            "drug_into" -> toJson(obj.getAs[String]("drug_into").get)
        )
    }
}