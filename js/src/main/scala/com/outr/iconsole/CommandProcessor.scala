package com.outr.iconsole

import scala.language.experimental.macros

trait CommandProcessor {
  def module: Option[String]
  def name: String

  def process(command: Command): Unit
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

  def registerFromObject[T](obj: T): List[CommandProcessor] = macro ProcessorGenerator.registerFromObject[T]

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
}