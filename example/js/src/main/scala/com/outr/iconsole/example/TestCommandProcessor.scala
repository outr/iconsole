package com.outr.iconsole.example

import com.outr.iconsole.{Command, CommandProcessor}

object TestCommandProcessor extends CommandProcessor {
  override def module: Option[String] = None
  override def name: String = "test"

  override def process(command: Command): Unit = scribe.info(s"Test command! ${command.args}")
}