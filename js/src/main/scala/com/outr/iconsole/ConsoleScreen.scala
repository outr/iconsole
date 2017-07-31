package com.outr.iconsole

import io.youi._
import io.youi.app.screen.{UIScreen, URLActivation}
import io.youi.net.{URL, URLMatcher}

class ConsoleScreen(override val matcher: URLMatcher) extends UIScreen with URLActivation {
  override def createUI(): Unit = {
    renderer.backgroundColor := ColorScheme.base3

    CommandInput.position.center := container.position.center
    CommandInput.position.bottom := container.position.bottom - 5.0
    container.children += CommandInput

    renderer.event.key.down.attach { evt =>
      if (evt.key == Key.Enter) {
        Command.parse(CommandInput.value()).foreach { command =>
          // TODO: support active module
          CommandProcessor.process(None, command).foreach { commandResult =>
            val resultContainer = new ResultContainer(command, commandResult)
            container.children += resultContainer
            scribe.info(s"Result: $commandResult")
          }
        }
      } else if (evt.key == Key.Tab) {
        evt.stopPropagation()
      }
    }
  }

  override def updateURL(current: URL): Option[HistoryStateChange] = None
}