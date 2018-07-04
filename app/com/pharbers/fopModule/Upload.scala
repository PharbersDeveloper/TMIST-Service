package com.pharbers.fopModule

import java.io.File
import java.util.UUID

import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import play.api.mvc.MultipartFormData
import play.api.libs.Files.TemporaryFile

import com.pharbers.TempLog._
import com.pharbers.ErrorCode.errorToJson

object Upload {
	// TODO 暂未实现文件上传功能
	def uploadFile(data: MultipartFormData[TemporaryFile]): JsValue = {
		try {
			val lst = data.files.map { file =>
				val path = "/mnt/"
				val uuid = UUID.randomUUID
				phTempLog("暂未实现文件上传功能")
				file.ref.moveTo(new File(s"$path/$uuid"), replace = true)
				uuid
			}
			phTempLog(s"上传文件，md5 = " + lst)
			toJson(Map("status" -> toJson("ok"), "result" -> toJson(lst)))
		} catch {
			case _: Exception =>
				phTempLog(errorToJson("upload error").toString)
				errorToJson("upload error")
		}
	}
}
