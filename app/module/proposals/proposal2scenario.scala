package module.proposals

import com.mongodb.casbah
import com.mongodb.casbah.Imports
import com.mongodb.casbah.Imports.{$or, DBObject, MongoDBObject, _}
import module.common.stragety.{bind, impl, one2many}
import module.proposals.scenarios.scenario
import org.bson.types.ObjectId
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by clock on 18-7-9.
  */
class proposal2scenario extends one2many[proposal, scenario] with bind[proposal, scenario] {
    override def createThis: proposal = impl[proposal]
    override def createThat: scenario = impl[scenario]

    override def one2manyssr(obj: Imports.DBObject): Map[String, JsValue] =
        Map("_id" -> toJson(obj.getAs[String]("scenario_id").get))

    override def one2manyaggregate(lst: List[Map[String, JsValue]]): DBObject =
        $or(lst map (x => DBObject("_id" -> new ObjectId(x("_id").asOpt[String].get))))

    override def one2manysdr(obj: Imports.DBObject): Map[String, JsValue] =
        Map(
            "condition" -> toJson(Map(
                "bind_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
                "proposal_id" -> toJson(obj.getAs[String]("proposal_id").get),
                "scenario_id" -> toJson(obj.getAs[String]("scenario_id").get)
            ))
        )

    override def bind(data: JsValue): Imports.DBObject = {
        val builder = MongoDBObject.newBuilder
        builder += "_id" -> ObjectId.get()
        builder += "proposal_id" -> (data \ "proposal" \ "proposal_id").asOpt[String].get
        builder += "scenario_id" -> (data \ "scenario" \ "scenario_id").asOpt[String].get

        builder.result
    }

    override def unbind(data: JsValue): casbah.Imports.DBObject = {
        val builder = MongoDBObject.newBuilder
        val _id = (data \ "condition" \ "bind_id").asOpt[String].get
        builder += "_id" -> new ObjectId(_id)

        builder.result
    }
}
