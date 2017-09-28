package com.outr.iconsole

import io.youi.{HistoryStateChange, Key, ui}
import io.youi.app.screen.{UIScreen, URLActivation}
import io.youi.component.Container
import io.youi.layout.VerticalLayout
import io.youi.net.{URL, URLMatcher}

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
          CommandProcessor.process(None, command).foreach { commandResult =>
            val resultContainer = new ResultContainer(command, commandResult)
            resultContainer.position.center := ui.position.center()
            ConsoleResults.children += resultContainer
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

object ConsoleResults extends Container {
  layoutManager := new VerticalLayout(10.0, fromTop = true)
  position.left := 0.0
  position.top := 0.0
  size.width := ui.width
  size.height := ui.height - CommandInput.size.height - 10.0
}