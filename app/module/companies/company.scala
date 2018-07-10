package module.companies

import org.bson.types.ObjectId
import play.api.libs.json.JsValue
import com.mongodb.casbah.Imports._
import play.api.libs.json.Json.toJson
import module.common.datamodel.basemodel
import module.common.checkExist.checkAttrExist

/**
  * Created by clock on 18-7-6.
  */
class company extends basemodel
        with checkAttrExist {

    override val name = "company"
    override lazy val names = "companies"
    override def runtimeClass: Class[_] = classOf[company]

    override val qc : JsValue => DBObject = { js =>
        val tmp = (js \ "data" \ "condition" \ "company_id").asOpt[String].get
        DBObject("_id" -> new ObjectId(tmp))
    }

    override val anqc: JsValue => DBObject = { js =>
        val tmp = (js \ "company" \ "company_id").asOpt[String].get
        DBObject("company_id" -> tmp)
    }

    override val qcm : JsValue => DBObject = js => DBObject()

    override val ssr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "company_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString)
        )
    }

    override val sr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "company_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "company_name" -> toJson(obj.getAs[String]("company_name").get)
        )
    }

    override val dr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "company_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "company_name" -> toJson(obj.getAs[String]("company_name").get),
            "company_des" -> toJson(obj.getAs[String]("company_des").get)
        )
    }

    override val popr : DBObject => Map[String, JsValue] = { _ =>
        Map(
            "pop company" -> toJson("success")
        )
    }

    override val d2m : JsValue => DBObject = { js =>
        val data = (js \ "data" \ "company").asOpt[JsValue].map (x => x).getOrElse(toJson(""))

        val builder = MongoDBObject.newBuilder
        builder += "_id" -> ObjectId.get()      // 唯一标示
        builder += "company_name" -> (data \ "company_name").asOpt[String].map (x => x).getOrElse("")
        builder += "company_des" -> (data \ "company_des").asOpt[String].map (x => x).getOrElse("")

        builder.result
    }

    override val up2m : (DBObject, JsValue) => DBObject = { (obj, js) =>
        val data = (js \ "data" \ "company").asOpt[JsValue].get

        (data \ "company_name").asOpt[String].map (x => obj += "company_name" -> x).getOrElse(Unit)
        (data \ "company_des").asOpt[String].map (x => obj += "company_des" -> x).getOrElse(Unit)

        obj
    }

    override val ckAttrExist: JsValue => DBObject = jv => DBObject("company_name" -> (jv \ "data" \ "company" \ "company_name").asOpt[String].get)

}
