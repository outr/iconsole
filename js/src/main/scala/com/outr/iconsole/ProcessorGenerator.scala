package com.outr.iconsole

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object ProcessorGenerator {
  def registerFromObject[T](c: blackbox.Context)(module: c.Expr[Option[String]], obj: c.Expr[T])(implicit t: c.WeakTypeTag[T]): c.Expr[List[CommandProcessor]] = {
    import c.universe._
    val processors = processorsFor[T](c)(module, obj)(t)
    c.Expr[List[CommandProcessor]](
      q"""
         $processors.foreach { p =>
           com.outr.iconsole.CommandProcessor.register(p)
         }
         $processors
       """)
  }

  def processorsFor[T](c: blackbox.Context)(module: c.Expr[Option[String]], obj: c.Expr[T])(implicit t: c.WeakTypeTag[T]): c.Expr[List[CommandProcessor]] = {
    import c.universe._

    val members = weakTypeOf[T].decls
    val methods = members.filter { m =>
      val term = m.asTerm
      term.isMethod && !term.isConstructor && term.isPublic && !m.name.decodedName.toString.contains("$default$")
    }
    val defaultArgs = members.filter { m =>
      val term = m.asTerm
      term.isMethod && !term.isConstructor && term.isPublic && m.name.decodedName.toString.contains("$default$")
    }.map(m => m.name.decodedName.toString -> m).toMap
    val commandProcessors = methods.map { m =>
      val name = m.name.decodedName.toString
      val paramList = m.info.paramLists.head
      val params = paramList.zipWithIndex.map {
        case (param, index) => {
          val paramName = param.name.decodedName.toString
          val paramType = param.info.resultType
          val defaultArg = defaultArgs.get(s"$name$$default$$${index + 1}").map(m => q"$m")
          q"com.outr.iconsole.ProcessorGenerator.extractArg[$paramType](command, $paramName, $index, $defaultArg)"
        }
      }
      q"""
         import com.outr.iconsole._

         CommandProcessor($module, $name) { command =>
           $m(..$params)
         }
      """
    }
    c.Expr[List[CommandProcessor]](q"List(..$commandProcessors)")
  }

  def extractArg[T](command: Command, name: String, index: Int, default: => Option[T])(implicit string2T: String => T): T = {
    val s = command.args.get(name).orElse(command.args.get(s"arg${index + 1}"))
    s.map(string2T).orElse(default).getOrElse(throw new RuntimeException(s"No argument provided $name (index: $index)."))
  }
}