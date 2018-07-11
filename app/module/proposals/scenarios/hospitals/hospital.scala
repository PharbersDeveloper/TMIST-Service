package module.proposals.scenarios.hospitals

import org.bson.types.ObjectId
import play.api.libs.json.JsValue
import com.mongodb.casbah.Imports._
import play.api.libs.json.Json.toJson
import module.common.datamodel.basemodel

/**
  * Created by clock on 18-7-10.
  */
class hospital extends basemodel {

    override val name = "hospital"
    override def runtimeClass: Class[_] = classOf[hospital]

    override val qc : JsValue => DBObject = { js =>
        val tmp = (js \ "data" \ "condition" \ "hospital_id").asOpt[String].get
        DBObject("_id" -> new ObjectId(tmp))
    }

    override val anqc: JsValue => DBObject = { js =>
        val tmp = (js \ "hospital" \ "hospital_id").asOpt[String].get
        DBObject("hospital_id" -> tmp)
    }

    override val qcm : JsValue => DBObject = _ => DBObject()

    override val ssr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "hospital_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString)
        )
    }

    override val sr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "hospital_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "hosp_name" -> toJson(obj.getAs[String]("hosp_name").get)
        )
    }

    override val dr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "hospital_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "hosp_name" -> toJson(obj.getAs[String]("hosp_name").get),
            "hosp_level" -> toJson(obj.getAs[String]("hosp_level").get)
        )
    }

    override val popr : DBObject => Map[String, JsValue] = { _ =>
        Map(
            "pop hospital" -> toJson("success")
        )
    }

    override val d2m : JsValue => DBObject = { js =>
        val data = (js \ "data" \ "hospital").asOpt[JsValue].map (x => x).getOrElse(toJson(""))

        val builder = MongoDBObject.newBuilder
        builder += "_id" -> ObjectId.get()      // hospital_id 唯一标示
        builder += "hosp_name" -> (data \ "hosp_name").asOpt[String].map (x => x).getOrElse("")
        builder += "hosp_level" -> (data \ "hosp_level").asOpt[String].map (x => x).getOrElse("")

        builder.result
    }

    override val up2m : (DBObject, JsValue) => DBObject = { (obj, js) =>
        val data = (js \ "data" \ "hospital").asOpt[JsValue].get

        (data \ "hosp_name").asOpt[String].map (x => obj += "hosp_name" -> x).getOrElse(Unit)
        (data \ "hosp_level").asOpt[String].map (x => obj += "hosp_level" -> x).getOrElse(Unit)

        obj
    }
}
