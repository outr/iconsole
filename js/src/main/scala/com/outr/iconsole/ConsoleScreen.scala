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
      scribe.info(s"KeyEvent: $evt")
    }
  }

  override def updateURL(current: URL): Option[HistoryStateChange] = None
}