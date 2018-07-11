package module.common.datamodel

import com.mongodb.casbah.Imports.{DBObject, ObjectId, _}
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

trait cdr {

    val dr: DBObject => Map[String, JsValue] = { obj =>
        obj.map { cell =>
            cell._2 match {
                case id: ObjectId => "id" -> toJson(id.toString)
                case str: String => cell._1 -> toJson(str)
                case i: java.lang.Integer => cell._1 -> toJson(i.toInt)
                case l: java.lang.Long => cell._1 -> toJson(l.toLong)
                case d: java.lang.Double => cell._1 -> toJson(d.toDouble)
                case obj: DBObject => cell._1 -> toJson(dr(obj))
                case lst: List[DBObject] => cell._1 -> toJson(lst.map(dr))
            }
        }.toMap
    }

}
