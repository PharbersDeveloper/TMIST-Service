package module.proposals.scenarios.representative

import org.bson.types.ObjectId
import play.api.libs.json.JsValue
import com.mongodb.casbah.Imports._
import play.api.libs.json.Json.toJson
import module.common.datamodel.basemodel

/**
  * Created by clock on 18-7-10.
  */
class representative extends basemodel {

    override val name = "representative"
    override def runtimeClass: Class[_] = classOf[representative]

    override val qc : JsValue => DBObject = { js =>
        val tmp = (js \ "data" \ "condition" \ "representative_id").asOpt[String].get
        DBObject("_id" -> new ObjectId(tmp))
    }

    override val anqc: JsValue => DBObject = { js =>
        val tmp = (js \ "representative" \ "representative_id").asOpt[String].get
        DBObject("representative_id" -> tmp)
    }

    override val qcm : JsValue => DBObject = _ => DBObject()

    override val ssr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "representative_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString)
        )
    }

    override val sr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "representative_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "rep_name" -> toJson(obj.getAs[String]("rep_name").get)
        )
    }

    override val dr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "representative_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "rep_name" -> toJson(obj.getAs[String]("rep_name").get),
            "rep_avatar" -> toJson(obj.getAs[String]("rep_avatar").get)
        )
    }

    override val popr : DBObject => Map[String, JsValue] = { _ =>
        Map(
            "pop representative" -> toJson("success")
        )
    }

    override val d2m : JsValue => DBObject = { js =>
        val data = (js \ "data" \ "representative").asOpt[JsValue].map (x => x).getOrElse(toJson(""))

        val builder = MongoDBObject.newBuilder
        builder += "_id" -> ObjectId.get()      // representative_id 唯一标示
        builder += "rep_name" -> (data \ "rep_name").asOpt[String].map (x => x).getOrElse("")
        builder += "rep_avatar" -> (data \ "rep_avatar").asOpt[String].map (x => x).getOrElse("")

        builder.result
    }

    override val up2m : (DBObject, JsValue) => DBObject = { (obj, js) =>
        val data = (js \ "data" \ "representative").asOpt[JsValue].get

        (data \ "rep_name").asOpt[String].map (x => obj += "rep_name" -> x).getOrElse(Unit)
        (data \ "rep_avatar").asOpt[String].map (x => obj += "rep_avatar" -> x).getOrElse(Unit)

        obj
    }
}
