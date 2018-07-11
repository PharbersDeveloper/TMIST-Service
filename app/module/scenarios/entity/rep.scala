package module.scenarios.entity

import com.mongodb.casbah.Imports.{DBObject, _}
import org.bson.types.ObjectId
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

//        "_id" : ObjectId("5b43564ded925c05565b5c33"),
//        "type" : "rep",
//        "rep_name" : "小宋",
//        "avatar" : "/assets/images/hosp_seller.png",
//        "age" : NumberInt(37),
//        "education" : "本科",
//        "profe_bg" : "医学院校临床专业毕业",
//        "service_year" : NumberInt(8),
//        "entry_time" : NumberInt(3),
//        "business_exp" : "抗生素、心血管类产",
//        "sales_skills" : "善于发现客户需求,善于探查客户心理",
//        "latest_news" : "最近由于同事得到提升而垂头丧气,对个人未来发展感到茫然"


@deprecated
object rep {
    val dr: DBObject => Map[String, JsValue] = { obj =>
        Map(
            "reso_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "type" -> toJson(obj.getAs[String]("type").get),
            "rep_name" -> toJson(obj.getAs[String]("rep_name").get),

            "avatar" -> toJson(obj.getAs[String]("avatar").get),
            "age" -> toJson(obj.getAs[Int]("age").get),
            "education" -> toJson(obj.getAs[String]("education").get),

            "profe_bg" -> toJson(obj.getAs[String]("profe_bg").get),
            "service_year" -> toJson(obj.getAs[Int]("service_year").get),
            "entry_time" -> toJson(obj.getAs[Int]("entry_time").get),

            "business_exp" -> toJson(obj.getAs[String]("business_exp").get),
            "sales_skills" -> toJson(obj.getAs[String]("sales_skills").get),
            "latest_news" -> toJson(obj.getAs[String]("latest_news").get)
        )
    }
}