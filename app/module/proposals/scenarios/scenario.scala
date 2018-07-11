package module.proposals.scenarios

import com.mongodb.casbah.Imports._
import module.common.datamodel.basemodel
import org.bson.types.ObjectId
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by clock on 18-7-9.
  */
class scenario extends basemodel {

    override val name = "scenario"
    override def runtimeClass: Class[_] = classOf[scenario]

    override val qc : JsValue => DBObject = { js =>
        val tmp = (js \ "data" \ "condition" \ "scenario_id").asOpt[String].get
        DBObject("_id" -> new ObjectId(tmp))
    }

    override val anqc: JsValue => DBObject = { js =>
        val tmp = (js \ "scenario" \ "scenario_id").asOpt[String].get
        DBObject("scenario_id" -> tmp)
    }

    override val qcm : JsValue => DBObject = _ => DBObject()

    override val ssr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "scenario_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString)
        )
    }

    override val sr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "scenario_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "current_month" -> toJson(obj.getAs[Int]("current_month").get)
        )
    }

    override val dr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "scenario_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "current_month" -> toJson(obj.getAs[Int]("current_month").get)
        )
    }

    override val popr : DBObject => Map[String, JsValue] = { _ =>
        Map(
            "pop scenario" -> toJson("success")
        )
    }

    override val d2m : JsValue => DBObject = { js =>
        val data = (js \ "data" \ "scenario").asOpt[JsValue].map (x => x).getOrElse(toJson(""))

        val builder = MongoDBObject.newBuilder
        builder += "_id" -> ObjectId.get()      // scenario_id 唯一标示
        builder += "current_month" -> (data \ "current_month").asOpt[Int].map (x => x).getOrElse("")

        builder.result
    }

    override val up2m : (DBObject, JsValue) => DBObject = { (obj, js) =>
        val data = (js \ "data" \ "scenario").asOpt[JsValue].get

        (data \ "current_month").asOpt[Int].map (x => obj += "current_month" -> int2Integer(x)).getOrElse(Unit)

        obj
    }
}
