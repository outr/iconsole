package com.outr.iconsole

import scala.collection.mutable.ListBuffer

case class Command(text: String,
                   module: Option[String],
                   name: String,
                   args: Map[String, String],
                   started: Long = System.currentTimeMillis())

object Command {
  private val CommandAndModuleRegex = """(.+):(.+)""".r
  private val NamedArgRegex = """(.+)=(.+)""".r

  def apply(text: String): Command = parse(text).getOrElse(throw new RuntimeException(s"Unable to parse command: $text"))

  def parse(s: String): Option[Command] = {
    val list = ListBuffer.empty[String]
    val b = new StringBuilder
    var openQuotes = false
    s.trim.foreach {
      case ' ' if !openQuotes => if (b.nonEmpty) {
        list += b.toString()
        b.clear()
      }
      case '"' => openQuotes = !openQuotes
      case c => b.append(c)
    }
    if (b.nonEmpty) {
      list += b.toString()
    }
    if (list.nonEmpty) {
      val (module, name) = list.head match {
        case CommandAndModuleRegex(m, n) => Some(m) -> n
        case n => None -> n
      }
      var increment = 0
      val args = list.tail.map {
        case NamedArgRegex(n, v) => n -> v
        case v => {
          increment += 1
          s"arg$increment" -> v
        }
      }
      Some(Command(s, module, name, args.toMap))
    } else {
      None
    }
  }
}