package com.outr.iconsole

import com.outr.iconsole.result.{CommandResult, TextResult}

import scala.concurrent.Future
import scala.language.experimental.macros

import scala.concurrent.ExecutionContext.Implicits.global

trait CommandProcessor {
  def module: Option[String]
  def name: String

  def process(command: Command): Future[CommandResult]
}

object CommandProcessor {
  IConsole    // Make sure IConsole is loaded

  private var map = Map.empty[String, CommandProcessor]

  def register(processor: CommandProcessor): Unit = synchronized {
    val key = processor.module match {
      case Some(m) => s"$m:${processor.name}"
      case None => processor.name
    }
    map += key -> processor
  }

  def registerFromObject[T](module: Option[String], obj: T): List[CommandProcessor] = macro ProcessorGenerator.registerFromObject[T]

  def process(module: Option[String], command: Command): Option[Future[CommandResult]] = {
    val list = command.module match {
      case Some(m) => List(s"$m:${command.name}")
      case None => List(module.map(m => s"$m:${command.name}"), Some(command.name)).flatten
    }
    list.view.flatMap(map.get).headOption.map(_.process(command))
  }

  def apply(module: Option[String], name: String)(processor: Command => Any): CommandProcessor = {
    new FunctionCommandProcessor(module, name, processor)
  }
}

class FunctionCommandProcessor(override val module: Option[String],
                               override val name: String,
                               processor: Command => Any) extends CommandProcessor {
  override def process(command: Command): Future[CommandResult] = {
    // TODO: support better
    Future.successful(CommandResult(successful = true, content = new TextResult(processor(command).toString)))
  }
}