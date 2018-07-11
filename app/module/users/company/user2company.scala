package module.users.company

import com.mongodb
import com.mongodb.casbah
import module.companies.company
import org.bson.types.ObjectId
import com.mongodb.casbah.Imports
import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import module.common.checkExist.checkBindExist
import module.common.stragety.{bind, impl, one2one}
import com.mongodb.casbah.Imports.{DBObject, MongoDBObject}
import module.users.user

/**
  * Created by spark on 18-7-5.
  */
class user2company extends one2one[user, company] with bind[user, company] {
    override def createThis: user = impl[user]
    override def createThat: company = impl[company]

    override def one2onessr(obj: DBObject): Map[String, JsValue] =
        Map("data" -> toJson(
            Map("condition" -> toJson(
                Map("company_id" -> toJson(obj.getAs[String]("company_id").get))
            ))
        ))

    override def one2onesdr(obj: mongodb.casbah.Imports.DBObject): Map[String, JsValue] =
        Map(
            "condition" -> toJson(Map(
                "bind_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
                "user_id" -> toJson(obj.getAs[String]("user_id").get),
                "company_id" -> toJson(obj.getAs[String]("company_id").get)
            ))
        )

    override def bind(data: JsValue): Imports.DBObject = {
        val builder = MongoDBObject.newBuilder
        builder += "_id" -> ObjectId.get()
        builder += "user_id" -> (data \ "user" \ "user_id").asOpt[String].get
        builder += "company_id" -> (data \ "company" \ "company_id").asOpt[String].get

        builder.result
    }

    override def unbind(data: JsValue): casbah.Imports.DBObject = {
        val builder = MongoDBObject.newBuilder
        val _id = (data \ "condition" \ "bind_id").asOpt[String].get
        builder += "_id" -> new ObjectId(_id)

        builder.result
    }

}