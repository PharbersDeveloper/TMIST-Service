package module.proposals.dests

import scala.reflect.macros.whitebox
import scala.language.experimental.macros

trait CaseClassMapConverter[C] {
    def toMap(c: C): Map[String,Any]
    def fromMap(m: Map[String,Any]): C
}

object CaseClassMapConverter {
    implicit def Materializer[C]: CaseClassMapConverter[C] = macro converterMacro[C]
    def converterMacro[C: c.WeakTypeTag](c: whitebox.Context): c.Tree = {
        import c.universe._
        import play.api.libs.json.{JsString, JsNumber, JsValue}

        val tpe = weakTypeOf[C]
        val fields = tpe.decls.collectFirst {
            case m: MethodSymbol if m.isPrimaryConstructor => m
        }.get.paramLists.head

        val companion = tpe.typeSymbol.companion
        val (toParams,fromParams) = fields.map { field =>
            val name = field.name.toTermName
            val decoded = name.decodedName.toString
            val rtype = tpe.decl(name).typeSignature

            val m2 = q"map($decoded)" match {
                case q"$a: JsNumber" =>
                    println("rtype" + a)
                    q"map($decoded).asInstanceOf[JsString].value"
//                case q"Int" => q"map($decoded).asInstanceOf[JsNumber].value"
            }
//            val m = q"map($decoded).asInstanceOf[JsString].value"


            (q"$decoded -> t.$name", m2)

        }.unzip

        q"""
       new CaseClassMapConverter[$tpe] {
        def toMap(t: $tpe): Map[String,Any] = Map(..$toParams)
        def fromMap(map: Map[String,Any]): $tpe = $companion(..$fromParams)
       }
      """
    }
}