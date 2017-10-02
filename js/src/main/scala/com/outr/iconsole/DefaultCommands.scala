package com.outr.iconsole

object DefaultCommands {
  def init(): Unit = {
    CommandProcessor.register("clear")(_ => clear())
  }

  def clear(): Unit = {
    ConsoleResults.children := Vector.empty
  }
}