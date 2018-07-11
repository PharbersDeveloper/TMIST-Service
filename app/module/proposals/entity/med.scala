package module.proposals.entity

import org.bson.types.ObjectId
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import com.mongodb.casbah.Imports.{DBObject, _}

//            "_id" : ObjectId("5b435533ed925c05565b5c2c"),
//            "tpye" : "med",
//            "med_name" : "口服抗生素",
//            "category" : "分类",
//            "image" : "图片",
//            "set_time" : NumberInt(1984),
//            "insure_type" : "医保类型",
//            "research_type" : "研发类型",
//            "ref_price" : NumberInt(10),
//            "unit_cost" : NumberInt(8),
//            "therapeutic_field" : "治疗领域",
//            "features" : "产品特点",
//            "competing_info" : "竞品信息",
//            "latest_news" : "近期动态"


object med {
    val dr: DBObject => Map[String, JsValue] = { obj =>
        Map(
            "goods_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "type" -> toJson(obj.getAs[String]("type").get),
            "med_name" -> toJson(obj.getAs[String]("med_name").get),

            "category" -> toJson(obj.getAs[String]("category").get),
            "image" -> toJson(obj.getAs[String]("image").get),
            "set_time" -> toJson(obj.getAs[Int]("set_time").get),

            "insure_type" -> toJson(obj.getAs[String]("insure_type").get),
            "research_type" -> toJson(obj.getAs[String]("research_type").get),
            "ref_price" -> toJson(obj.getAs[Int]("ref_price").get),

            "unit_cost" -> toJson(obj.getAs[Int]("unit_cost").get),
            "therapeutic_field" -> toJson(obj.getAs[String]("therapeutic_field").get),
            "features" -> toJson(obj.getAs[String]("features").get),

            "competing_info" -> toJson(obj.getAs[String]("competing_info").get),
            "latest_news" -> toJson(obj.getAs[String]("latest_news").get)
        )
    }
}