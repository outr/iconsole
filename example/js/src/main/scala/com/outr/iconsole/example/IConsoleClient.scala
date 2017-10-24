package com.outr.iconsole.example

import com.outr.iconsole.{CommandInput, CommandProcessor, ConsoleScreen, IConsoleApplication}
import io.youi.app.ClientApplication
import io.youi.http.path

import scala.scalajs.js.annotation.JSExportTopLevel

object IConsoleClient extends ClientApplication with IConsoleApplication {
  val screen = new ConsoleScreen(path.exact("/"))

  @JSExportTopLevel("application")
  def main(): Unit = {
    CommandProcessor.register(TestCommandProcessor)
    CommandProcessor.registerFromObject(None, ExampleCommands)

    CommandInput.processValue("commands", includeInHistory = false)
  }
}