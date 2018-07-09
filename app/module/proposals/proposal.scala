package module.proposals

import org.bson.types.ObjectId
import play.api.libs.json.JsValue
import com.mongodb.casbah.Imports._
import play.api.libs.json.Json.toJson
import module.common.datamodel.basemodel

/**
  * Created by clock on 18-7-6.
  */
class proposal extends basemodel {

    override val name = "proposal"
    override def runtimeClass: Class[_] = classOf[proposal]

    override val qc : JsValue => DBObject = { js =>
        val tmp = (js \ "data" \ "condition" \ "proposal_id").asOpt[String].get
        DBObject("_id" -> new ObjectId(tmp))
    }

    override val anqc: JsValue => DBObject = { js =>
        val tmp = (js \ "proposal" \ "proposal_id").asOpt[String].get
        DBObject("proposal_id" -> tmp)
    }

    override val qcm : JsValue => DBObject = { js =>
        (js \ "proposal" \ "proposals").asOpt[List[String]].get match {
            case Nil => DBObject("query" -> "none")
            case ll : List[String] => $or(ll map (x => DBObject("proposal_name" -> x)))
        }
    }

    override val ssr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "proposal_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString)
        )
    }

    override val sr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "proposal_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "proposal_name" -> toJson(obj.getAs[String]("proposal_name").get)
        )
    }

    override val dr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "proposal_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "proposal_name" -> toJson(obj.getAs[String]("proposal_name").get),
            "proposal_des" -> toJson(obj.getAs[String]("proposal_des").get)
        )
    }

    override val popr : DBObject => Map[String, JsValue] = { _ =>
        Map(
            "pop proposal" -> toJson("success")
        )
    }

    override val d2m : JsValue => DBObject = { js =>
        val data = (js \ "data" \ "proposal").asOpt[JsValue].map (x => x).getOrElse(toJson(""))

        val builder = MongoDBObject.newBuilder
        builder += "_id" -> ObjectId.get()      // proposal_id 唯一标示
        builder += "proposal_name" -> (data \ "proposal_name").asOpt[String].map (x => x).getOrElse("")
        builder += "proposal_des" -> (data \ "proposal_des").asOpt[String].map (x => x).getOrElse("")

        builder.result
    }

    override val up2m : (DBObject, JsValue) => DBObject = { (obj, js) =>
        val data = (js \ "data" \ "proposal").asOpt[JsValue].get

        (data \ "proposal_name").asOpt[String].map (x => obj += "proposal_name" -> x).getOrElse(Unit)
        (data \ "proposal_des").asOpt[String].map (x => obj += "proposal_des" -> x).getOrElse(Unit)

        obj
    }
}
