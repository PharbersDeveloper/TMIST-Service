package module.scenarios

import org.bson.types.ObjectId
import play.api.libs.json.JsValue
import com.mongodb.casbah.Imports._
import module.common.datamodel.cdr

/**
  * Created by clock on 18-7-11.
  */
class scenario extends cdr {

    val name = "scenario"
    val names = "scenarios"

    val qc : JsValue => DBObject = { js =>
        val tmp = (js \ "data" \ "condition" \ "proposal_id").asOpt[String].get
        DBObject("_id" -> new ObjectId(tmp))
    }

//    override val popr : DBObject => Map[String, JsValue] = { _ =>
//        Map(
//            "pop proposal" -> toJson("success")
//        )
//    }
//
//    override val d2m : JsValue => DBObject = { js =>
//        val data = (js \ "data" \ "proposal").asOpt[JsValue].map (x => x).getOrElse(toJson(""))
//
//        val builder = MongoDBObject.newBuilder
//        builder += "_id" -> ObjectId.get()      // proposal_id 唯一标示
//        builder += "proposal_name" -> (data \ "proposal_name").asOpt[String].map (x => x).getOrElse("")
//        builder += "proposal_des" -> (data \ "proposal_des").asOpt[String].map (x => x).getOrElse("")
//
//        builder.result
//    }
//
//    override val up2m : (DBObject, JsValue) => DBObject = { (obj, js) =>
//        val data = (js \ "data" \ "proposal").asOpt[JsValue].get
//
//        (data \ "proposal_name").asOpt[String].map (x => obj += "proposal_name" -> x).getOrElse(Unit)
//        (data \ "proposal_des").asOpt[String].map (x => obj += "proposal_des" -> x).getOrElse(Unit)
//
//        obj
//    }
}
