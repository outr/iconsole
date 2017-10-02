package com.outr.iconsole

object DefaultCommands {
  def init(): Unit = {
    CommandProcessor.register("commands")(_ => commands())
    CommandProcessor.register("clear")(_ => clear())
  }

  def commands(): List[String] = {
    CommandProcessor.commands
  }

  def clear(): Unit = {
    ConsoleResults.children := Vector.empty
  }
}