package com.outr.iconsole

import scala.language.experimental.macros

trait CommandProcessor {
  def module: Option[String]
  def name: String

  def process(command: Command): Any
}

object CommandProcessor {
  private var map = Map.empty[String, CommandProcessor]

  def register(processor: CommandProcessor): Unit = synchronized {
    val key = processor.module match {
      case Some(m) => s"$m:${processor.name}"
      case None => processor.name
    }
    map += key -> processor
  }

  def registerFromObject[T](module: Option[String], obj: T): List[CommandProcessor] = macro ProcessorGenerator.registerFromObject[T]

  def process(module: Option[String], command: Command): Boolean = {
    val list = command.module match {
      case Some(m) => List(s"$m:${command.name}")
      case None => List(module.map(m => s"$m:${command.name}"), Some(command.name)).flatten
    }
    list.view.flatMap(map.get).headOption match {
      case Some(p) => {
        p.process(command)
        true
      }
      case None => false
    }
  }

  def apply(module: Option[String], name: String)(processor: Command => Any): CommandProcessor = {
    new FunctionCommandProcessor(module, name, processor)
  }
}

class FunctionCommandProcessor(override val module: Option[String],
                               override val name: String,
                               processor: Command => Any) extends CommandProcessor {
  override def process(command: Command): Any = processor(command)
}