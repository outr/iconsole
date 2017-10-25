package com.outr.iconsole

import scala.language.experimental.macros
import scala.language.implicitConversions
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
      val description = m.annotations.find(_.tree.tpe <:< typeOf[description]).flatMap { a =>
        a.tree.children.tail.collect({ case Literal(Constant(value: String)) => value }).headOption
      }.getOrElse("")
      val shortDescription = m.annotations.find(_.tree.tpe <:< typeOf[shortDescription]).flatMap { a =>
        a.tree.children.tail.collect({ case Literal(Constant(value: String)) => value }).headOption
      }.getOrElse("")
      val name = m.name.decodedName.toString
      val paramList = m.info.paramLists.head
      var arguments = List.empty[c.Tree]
      val params = paramList.zipWithIndex.map {
        case (param, index) => {
          val paramName = param.name.decodedName.toString
          val paramType = param.info.resultType
          val defaultArg: Option[c.Tree] = defaultArgs.get(s"$name$$default$$${index + 1}").map(m => q"$m")

          def extractString: c.Tree = q"com.outr.iconsole.ProcessorGenerator.extractArg[String](command, $paramName, $index, $defaultArg)"

          val a = q"new com.outr.iconsole.Argument($paramName, $index, ${paramType.toString}, $defaultArg)"
          arguments = a :: arguments

          if (paramType =:= typeOf[Boolean]) {
            q"$extractString.toBoolean"
          } else if (paramType =:= typeOf[Int]) {
            q"$extractString.toInt"
          } else if (paramType =:= typeOf[Long]) {
            q"$extractString.toLong"
          } else if (paramType =:= typeOf[Float]) {
            q"$extractString.toFloat"
          } else if (paramType =:= typeOf[Double]) {
            q"$extractString.toDouble"
          } else if (paramType =:= typeOf[Option[Boolean]]) {
            q"Option($extractString).map(_.toBoolean)"
          } else if (paramType =:= typeOf[Option[Int]]) {
            q"Option($extractString).map(_.toInt)"
          } else if (paramType =:= typeOf[Option[Long]]) {
            q"Option($extractString).map(_.toLong)"
          } else if (paramType =:= typeOf[Option[Float]]) {
            q"Option($extractString).map(_.toFloat)"
          } else if (paramType =:= typeOf[Option[Double]]) {
            q"Option($extractString).map(_.toDouble)"
          } else {
            q"""
               import com.outr.iconsole.DefaultConversions._      // Default implicits

               com.outr.iconsole.ProcessorGenerator.extractArg[$paramType](command, $paramName, $index, $defaultArg)
             """
          }
        }
      }
      arguments = arguments.reverse
      q"""
         import com.outr.iconsole._

         CommandProcessor($name, $module, $shortDescription, $description, Vector(..$arguments), autoRegister = false) { command =>
           $m(..$params)
         }
      """
    }
    c.Expr[List[CommandProcessor]](q"List(..$commandProcessors)")
  }

  def extractArg[T](command: Command, name: String, index: Int, default: => Option[T])(implicit string2T: String => T): T = {
    val s = command.args.get(name).orElse(command.args.get(s"arg${index + 1}"))
    s.map(string2T).orElse(default).getOrElse(throw new RuntimeException(s"No argument provided for parameter `$name` (index: $index)."))
  }
}

class Argument(val name: String, val index: Int, val `type`: String, defaultGetter: => Option[Any]) {
  def default: Option[Any] = defaultGetter

  override def toString: String = default match {
    case Some(d) => {
      val defaultString = if (`type` == "String") {
        s""""$d""""
      } else {
        d.toString
      }
      s"$name: ${`type`} = $defaultString"
    }
    case None => s"$name: ${`type`}"
  }
}