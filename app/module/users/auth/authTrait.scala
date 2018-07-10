package module.users.auth

import java.util.Date

import com.pharbers.TempLog
import play.api.libs.json.JsValue
import com.mongodb.casbah.Imports._
import play.api.libs.json.Json.toJson

import scala.collection.immutable.Map
import com.pharbers.sercuity.Sercurity
import com.pharbers.bmmessages.CommonModules
import com.pharbers.driver.PhRedisDriverImpl
import com.pharbers.dbManagerTrait.dbInstanceManager

/**
  * Created by clock on 18-6-7.
  */
trait authTrait {

    val authPwd: JsValue => DBObject = { js =>
        $and(
            DBObject("email" -> (js \ "data" \ "condition" \ "email").asOpt[String].map(x => x).getOrElse("")),
            DBObject("password" -> (js \ "data" \ "condition" \ "password").asOpt[String].map(x => x).getOrElse(""))
        )
    }

    def authWithPassword(func: JsValue => DBObject,
                         func_out: DBObject => Map[String, JsValue])
                        (data: JsValue)(db_name: String)
                        (implicit cm: CommonModules): Map[String, JsValue] = {

        val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
        val db = conn.queryDBInstance("client").get

        db.queryObject(func(data), db_name)(func_out) match {
            case None => throw new Exception("email or password error")
            case Some(one) => one
        }
    }

    def authSetExpire(data: JsValue)
                     (implicit cm: CommonModules): Map[String, JsValue] = {

        val rd = cm.modules.get.get("rd").map(x => x.asInstanceOf[PhRedisDriverImpl]).getOrElse(throw new Exception("no redis connection"))
        val expire = (data \ "data" \ "condition" \ "token_expire").asOpt[Int].map(x => x).getOrElse(24 * 60 * 60) //default expire in 24h
        val uid = (data \ "user" \ "user_id").asOpt[String].map(x => x).get
        val accessToken = "bearer" + Sercurity.md5Hash(uid + new Date().getTime)

        TempLog.phTempLog(s"user detial info = $data")
        TempLog.phTempLog(s"accessToken = $accessToken")

        rd.addMap(accessToken, "user_id", uid)
//        (data \ "user").asOpt[Map[String, JsValue]].get
//                .foreach { x =>
//                    rd.addMap(accessToken, x._1, x._2.asOpt[String].getOrElse(x._2.toString))
//                }
//        rd.expire(accessToken, expire)

        Map(
            "user_token" -> toJson(accessToken)
        )
    }

    def authParseToken(data: JsValue)
                     (implicit cm: CommonModules): Map[String, JsValue] = {

        val rd = cm.modules.get.get("rd").map(x => x.asInstanceOf[PhRedisDriverImpl]).getOrElse(throw new Exception("no redis connection"))
        val token = (data \ "token").asOpt[String].get
        if(!rd.exsits(token)) throw new Exception("token expired")
        Map("user" -> toJson(rd.getMapAllValue(token).map(x => x._1 -> toJson(x._2))))
    }

}
