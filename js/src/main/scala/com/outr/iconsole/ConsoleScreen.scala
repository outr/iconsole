package com.outr.iconsole

import com.outr.iconsole.result.{CommandResult, TextResult}
import io.youi.{HistoryStateChange, Key, ui}
import io.youi.app.screen.{UIScreen, URLActivation}
import io.youi.net.{URL, URLMatcher}

import scala.concurrent.Future

class ConsoleScreen(override val matcher: URLMatcher) extends UIScreen with URLActivation {
  override def createUI(): Unit = {
    container.background := ColorScheme.base3

    container.children += ConsoleResults
    CommandInput.position.center := container.position.center
    CommandInput.position.bottom := container.position.bottom - 5.0
    container.children += CommandInput


    ui.event.key.down.attach { evt =>
      if (evt.key == Key.Enter) {
        Command.parse(CommandInput.value()).foreach { command =>
          // TODO: support active module
          CommandProcessor.process(None, command) match {
            case Some(commandResult) => {
              val resultContainer = new ResultContainer(command, commandResult)
              resultContainer.position.center := ui.position.center()
              ConsoleResults.children += resultContainer
            }
            case None => {
              val result = new TextResult("Command not found!")
              val resultContainer = new ResultContainer(command, Future.successful(CommandResult(successful = false, content = result)))
              ConsoleResults.children += resultContainer
            }
          }
          CommandInput.value := ""
        }
      } else if (evt.key == Key.Tab) {
        evt.stopPropagation()
      }
    }
  }

  override def updateURL(current: URL): Option[HistoryStateChange] = None
}

