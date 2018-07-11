package module.proposals

import play.api.libs.json.JsValue
import com.mongodb.casbah.Imports._
import play.api.libs.json.Json.toJson
import com.mongodb.casbah.Imports.{DBObject, ObjectId}

package object entity {
    val dr: String => DBObject => Map[String, JsValue] = {
        case "rep" => rep.dr
        case "hosp" => hosp.dr
        case "med" => med.dr
    }

    val common: DBObject => Map[String, JsValue] = { obj =>
        obj.map { cell =>
            cell._2 match {
                case id: ObjectId => cell._1 -> toJson(id.toString)
                case str: String => cell._1 -> toJson(str)
                case i: java.lang.Integer => cell._1 -> toJson(i.toInt)
                case l: java.lang.Long => cell._1 -> toJson(l.toLong)
                case d: java.lang.Double => cell._1 -> toJson(d.toDouble)
                case obj: DBObject => cell._1 -> toJson(common(obj))
                case lst: List[DBObject] => cell._1 -> toJson(lst.map(common))
            }
        }.toMap
    }
}
