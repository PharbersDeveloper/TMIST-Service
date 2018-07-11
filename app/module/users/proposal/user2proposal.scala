package module.users.proposal

import module.users.user
import com.mongodb.casbah
import org.bson.types.ObjectId
import module.proposals.proposal
import com.mongodb.casbah.Imports
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import module.common.stragety.{bind, impl, one2many}
import com.mongodb.casbah.Imports.{DBObject, MongoDBObject, _}

/**
  * Created by spark on 18-7-5.
  */
class user2proposal extends one2many[user, proposal] with bind[user, proposal] {
    override def createThis: user = impl[user]
    override def createThat: proposal = impl[proposal]

    override def one2manyssr(obj: Imports.DBObject): Map[String, JsValue] =
        Map("_id" -> toJson(obj.getAs[String]("proposal_id").get))

    override def one2manyaggregate(lst: List[Map[String, JsValue]]): DBObject =
        $or(lst map (x => DBObject("_id" -> new ObjectId(x("_id").asOpt[String].get))))

    override def one2manysdr(obj: Imports.DBObject): Map[String, JsValue] =
        Map(
            "condition" -> toJson(Map(
                "bind_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
                "role_id" -> toJson(obj.getAs[String]("role_id").get),
                "user_id" -> toJson(obj.getAs[String]("user_id").get)
            ))
        )

    override def bind(data: JsValue): Imports.DBObject = {
        val builder = MongoDBObject.newBuilder
        builder += "_id" -> ObjectId.get()
        builder += "role_id" -> (data \ "role" \ "role_id").asOpt[String].get
        builder += "user_id" -> (data \ "user" \ "user_id").asOpt[String].get

        builder.result
    }

    override def unbind(data: JsValue): casbah.Imports.DBObject = {
        val builder = MongoDBObject.newBuilder
        val _id = (data \ "condition" \ "bind_id").asOpt[String].get
        builder += "_id" -> new ObjectId(_id)

        builder.result
    }

}