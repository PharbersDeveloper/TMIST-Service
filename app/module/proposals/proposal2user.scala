package module.proposals

import module.users.user
import com.mongodb.casbah
import org.bson.types.ObjectId
import com.mongodb.casbah.Imports
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import module.common.stragety.{bind, impl, one2many}
import com.mongodb.casbah.Imports.{$or, DBObject, MongoDBObject, _}

/**
  * Created by clock on 18-7-6.
  */
class proposals2user extends one2many[proposal, user] with bind[proposal, user] {
    override def createThis: proposal = impl[proposal]
    override def createThat: user = impl[user]

    override def one2manyssr(obj: Imports.DBObject): Map[String, JsValue] =
        Map("_id" -> toJson(obj.getAs[String]("user_id").get))

    override def one2manyaggregate(lst: List[Map[String, JsValue]]): DBObject =
        $or(lst map (x => DBObject("_id" -> new ObjectId(x("_id").asOpt[String].get))))

    override def one2manysdr(obj: Imports.DBObject): Map[String, JsValue] =
        Map(
            "condition" -> toJson(Map(
                "bind_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
                "user_id" -> toJson(obj.getAs[String]("user_id").get),
                "proposal_id" -> toJson(obj.getAs[String]("proposal_id").get)
            ))
        )

    override def bind(data: JsValue): Imports.DBObject = {
        val builder = MongoDBObject.newBuilder
        builder += "_id" -> ObjectId.get()
        builder += "user_id" -> (data \ "user" \ "user_id").asOpt[String].get
        builder += "proposal_id" -> (data \ "proposal" \ "proposal_id").asOpt[String].get

        builder.result
    }

    override def unbind(data: JsValue): casbah.Imports.DBObject = {
        val builder = MongoDBObject.newBuilder
        val _id = (data \ "condition" \ "bind_id").asOpt[String].get
        builder += "_id" -> new ObjectId(_id)

        builder.result
    }
}
