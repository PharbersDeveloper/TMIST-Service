package module.proposals.scenarios.products

import com.mongodb.casbah.Imports._
import module.common.datamodel.basemodel
import org.bson.types.ObjectId
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by clock on 18-7-10.
  */
class product extends basemodel {

    override val name = "product"
    override def runtimeClass: Class[_] = classOf[product]

    override val qc : JsValue => DBObject = { js =>
        val tmp = (js \ "data" \ "condition" \ "product_id").asOpt[String].get
        DBObject("_id" -> new ObjectId(tmp))
    }

    override val anqc: JsValue => DBObject = { js =>
        val tmp = (js \ "product" \ "product_id").asOpt[String].get
        DBObject("product_id" -> tmp)
    }

    override val qcm : JsValue => DBObject = _ => DBObject()

    override val ssr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "product_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString)
        )
    }

    override val sr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "product_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "prod_name" -> toJson(obj.getAs[String]("prod_name").get)
        )
    }

    override val dr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "product_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "prod_name" -> toJson(obj.getAs[String]("prod_name").get)
//            ,
//            "hosp_level" -> toJson(obj.getAs[String]("hosp_level").get)
        )
    }

    override val popr : DBObject => Map[String, JsValue] = { _ =>
        Map(
            "pop product" -> toJson("success")
        )
    }

    override val d2m : JsValue => DBObject = { js =>
        val data = (js \ "data" \ "product").asOpt[JsValue].map (x => x).getOrElse(toJson(""))

        val builder = MongoDBObject.newBuilder
        builder += "_id" -> ObjectId.get()      // product_id 唯一标示
        builder += "prod_name" -> (data \ "prod_name").asOpt[String].map (x => x).getOrElse("")
//        builder += "hosp_level" -> (data \ "hosp_level").asOpt[String].map (x => x).getOrElse("")

        builder.result
    }

    override val up2m : (DBObject, JsValue) => DBObject = { (obj, js) =>
        val data = (js \ "data" \ "product").asOpt[JsValue].get

        (data \ "prod_name").asOpt[String].map (x => obj += "prod_name" -> x).getOrElse(Unit)
//        (data \ "hosp_level").asOpt[String].map (x => obj += "hosp_level" -> x).getOrElse(Unit)

        obj
    }
}
