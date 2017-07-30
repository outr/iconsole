package com.outr.iconsole.example

object ExampleCommands {
  def echo(message: String = "Wahoo!"): Unit = {
    scribe.info(s"Echo: $message")
  }

  def alert(): Unit = scribe.info("Alert!")
}
