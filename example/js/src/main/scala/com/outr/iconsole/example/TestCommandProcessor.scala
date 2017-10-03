package com.outr.iconsole.example

import com.outr.iconsole.result.{CommandResult, TextResult}
import com.outr.iconsole.{Command, CommandProcessor}

import scala.concurrent.{Future, Promise}

import org.scalajs.dom._

object TestCommandProcessor extends CommandProcessor {
  override def module: Option[String] = None
  override def name: String = "test"
  override def shortDescription: String = description
  override def description: String = "Simple test command."

  override def process(command: Command): Future[CommandResult] = {
    val promise = Promise[CommandResult]
    window.setTimeout(() => {
      scribe.info(s"Test command! ${command.args}")
      promise.success(CommandResult(successful = true, new TextResult("Completed TestCommand!")))
    }, 2000.0)
    promise.future
  }
}