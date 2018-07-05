package module.users

import org.bson.types.ObjectId
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import com.mongodb.casbah.Imports._
import module.common.datamodel.basemodel
import module.common.checkExist.checkAttrExist
import module.users.auth.authTrait

/**
  * Created by spark on 18-4-19.
  */
class user extends basemodel
        with checkAttrExist with authTrait {

    override val name = "user"
    override def runtimeClass: Class[_] = classOf[user]

    override val qc : JsValue => DBObject = { js =>
        val tmp = (js \ "data" \ "condition" \ "user_id").asOpt[String].get
        DBObject("_id" -> new ObjectId(tmp))
    }

    override val anqc: JsValue => DBObject = { js =>
        val tmp = (js \ "user" \ "user_id").asOpt[String].get
        DBObject("user_id" -> tmp)
    }

    override val qcm : JsValue => DBObject = { js =>
        (js \ "data" \ "condition" \ "users").asOpt[List[String]].get match {
            case Nil => DBObject("query" -> "none")
            case ll : List[String] => $or(ll map (x => DBObject("_id" -> new ObjectId(x))))
        }
    }

    override val ssr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "user_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString)
        )
    }

    override val sr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "user_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "email" -> toJson(obj.getAs[String]("email").get),
            "user_name" -> toJson(obj.getAs[String]("user_name").get)
        )
    }

    override val dr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "user_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "email" -> toJson(obj.getAs[String]("email").get),
            "password" -> toJson(obj.getAs[String]("password").get),
            "user_name" -> toJson(obj.getAs[String]("user_name").get),
            "user_phone" -> toJson(obj.getAs[String]("user_phone").get)
        )
    }

    override val popr : DBObject => Map[String, JsValue] = { _ =>
        Map(
            "pop user" -> toJson("success")
        )
    }

    override val d2m : JsValue => DBObject = { js =>
        val data = (js \ "data" \ "user").asOpt[JsValue].map (x => x).getOrElse(toJson(""))

        val builder = MongoDBObject.newBuilder
        builder += "_id" -> ObjectId.get()
        builder += "email" -> (data \ "email").asOpt[String].map (x => x).getOrElse("")
        builder += "password" -> (data \ "password").asOpt[String].map (x => x).getOrElse("")
        builder += "user_name" -> (data \ "user_name").asOpt[String].map (x => x).getOrElse("")
        builder += "user_phone" -> (data \ "user_phone").asOpt[String].map (x => x).getOrElse("")

        builder.result
    }

    override val up2m : (DBObject, JsValue) => DBObject = { (obj, js) =>
        val data = (js \ "data" \ "user").asOpt[JsValue].get

        (data \ "email").asOpt[String].map (x => obj += "email" -> x).getOrElse(Unit)
        (data \ "password").asOpt[String].map (x => obj += "password" -> x).getOrElse(Unit)
        (data \ "user_name").asOpt[String].map (x => obj += "user_name" -> x).getOrElse(Unit)
        (data \ "user_phone").asOpt[String].map (x => obj += "user_phone" -> x).getOrElse(Unit)

        obj
    }

    override val ckAttrExist: JsValue => DBObject = jv => DBObject("email" -> (jv \ "data" \ "user" \ "email").asOpt[String].get)

}