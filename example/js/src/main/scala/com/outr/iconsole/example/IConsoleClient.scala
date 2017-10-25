package com.outr.iconsole.example

import com.outr.iconsole._
import io.youi.app.ClientApplication
import io.youi.http.path

import scala.scalajs.js.annotation.JSExportTopLevel

object IConsoleClient extends ClientApplication with IConsoleApplication {
  val screen = new ConsoleScreen(path.exact("/"))

  @JSExportTopLevel("application")
  def main(): Unit = {
    IConsole.register(TestCommandProcessor)
    IConsole.registerFromObject(None, ExampleCommands)

    IConsole.execute(Command("commands"), includeInHistory = false)
  }
}