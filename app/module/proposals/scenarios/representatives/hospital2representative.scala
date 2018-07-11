package module.proposals.scenarios.representatives

import com.mongodb.casbah
import org.bson.types.ObjectId
import com.mongodb.casbah.Imports
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import module.common.stragety.{bind, impl, one2many}
import module.proposals.scenarios.hospitals.hospital
import com.mongodb.casbah.Imports.{$or, DBObject, MongoDBObject, _}

/**
  * Created by clock on 18-7-6.
  */
class hospital2representative extends one2many[hospital, representative] with bind[hospital, representative] {
    override def createThis: hospital = impl[hospital]
    override def createThat: representative = impl[representative]

    override def one2manyssr(obj: Imports.DBObject): Map[String, JsValue] =
        Map("_id" -> toJson(obj.getAs[String]("representative_id").get))

    override def one2manyaggregate(lst: List[Map[String, JsValue]]): DBObject =
        $or(lst map (x => DBObject("_id" -> new ObjectId(x("_id").asOpt[String].get))))

    override def one2manysdr(obj: Imports.DBObject): Map[String, JsValue] =
        Map(
            "condition" -> toJson(Map(
                "bind_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
                "hospital_id" -> toJson(obj.getAs[String]("hospital_id").get),
                "representative_id" -> toJson(obj.getAs[String]("representative_id").get)
            ))
        )

    override def bind(data: JsValue): Imports.DBObject = {
        val builder = MongoDBObject.newBuilder
        builder += "_id" -> ObjectId.get()
        builder += "hospital_id" -> (data \ "hospital" \ "hospital_id").asOpt[String].get
        builder += "representative_id" -> (data \ "representative" \ "representative_id").asOpt[String].get

        builder.result
    }

    override def unbind(data: JsValue): casbah.Imports.DBObject = {
        val builder = MongoDBObject.newBuilder
        val _id = (data \ "condition" \ "bind_id").asOpt[String].get
        builder += "_id" -> new ObjectId(_id)

        builder.result
    }
}