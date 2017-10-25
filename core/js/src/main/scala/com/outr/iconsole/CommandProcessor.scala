package com.outr.iconsole

import com.outr.iconsole.result.{CommandResult, TextResult}
import io.youi.component.{Component, Container}
import io.youi.layout.VerticalLayout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.experimental.macros

trait CommandProcessor {
  def module: Option[String]
  def name: String
  def arguments: Vector[Argument] = Vector.empty
  def shortDescription: String = "No description available."
  def description: String = "No description available."

  lazy val syntax: String = s"$name(${arguments.mkString(", ")})"

  def process(command: Command): Future[CommandResult]
}

object CommandProcessor {
  private var map = Map.empty[String, CommandProcessor]

  def commands: List[CommandProcessor] = map.values.toList.sortBy(_.name)

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
    list.view.flatMap(map.get).headOption.map { cp =>
      try {
        cp.process(command)
      } catch {
        case t: Throwable => CommandProcessor.value2CommandResult(t)
      }
    }
  }

  def apply(name: String,
            module: Option[String] = None,
            shortDescription: String = "",
            description: String = "",
            arguments: Vector[Argument],
            autoRegister: Boolean = true)(processor: Command => Any): CommandProcessor = {
    val ld = if (description.isEmpty) "No description available." else description
    val sd = if (shortDescription.isEmpty) ld.take(100) else shortDescription
    val p = new FunctionCommandProcessor(module, name, processor, sd, ld, arguments)
    if (autoRegister) register(p)
    p
  }

  def value2CommandResult(value: Any): Future[CommandResult] = value match {
    case () => value2CommandResult("Finished successfully")
    case f: Future[_] => f.flatMap(value2CommandResult)
    case c: Component => Future.successful(CommandResult(successful = true, content = c))
    case l: List[_] => {
      val futures = Future.sequence(l.map(value2CommandResult))
      futures.map { results =>
        val container = new Container
        container.layout := new VerticalLayout(10.0)
        container.children ++= results.map(_.content)
        CommandResult(successful = results.forall(_.successful), content = container)
      }
    }
    case t: Throwable => Future.successful(CommandResult(successful = false, content = new TextResult(s"${t.getLocalizedMessage} (${t.getClass.getSimpleName})")))
    case _ => Future.successful(CommandResult(successful = true, content = new TextResult(value.toString)))
  }
}

class FunctionCommandProcessor(override val module: Option[String],
                               override val name: String,
                               processor: Command => Any,
                               override val shortDescription: String,
                               override val description: String,
                               override val arguments: Vector[Argument]) extends CommandProcessor {
  override def process(command: Command): Future[CommandResult] = CommandProcessor.value2CommandResult(processor(command))
}