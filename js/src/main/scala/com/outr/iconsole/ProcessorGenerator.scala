package com.outr.iconsole

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object ProcessorGenerator {
  def registerFromObject[T](c: blackbox.Context)(obj: c.Expr[T])(implicit t: c.WeakTypeTag[T]): c.Expr[List[CommandProcessor]] = {
    import c.universe._

    // TODO: module support

    val members = weakTypeOf[T].decls
    val methods = members.filter { m =>
      val term = m.asTerm
      term.isMethod && !term.isConstructor && term.isPublic && !m.name.decodedName.toString.contains("$default$")
    }
    methods.foreach { m =>
      val name = m.name.decodedName.toString
      println(s"Member: $name")
      m.info.paramLists.foreach { paramList =>
        paramList.foreach { param =>
          val paramName = param.name.decodedName.toString
          println(s"\t$paramName: ${param.info.resultType}")
        }
      }
    }

    c.abort(c.enclosingPosition, "Work in progress")
  }
}