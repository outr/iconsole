package com.outr.iconsole

import io.youi._
import io.youi.app.screen.{UIScreen, URLActivation}
import io.youi.net.{URL, URLMatcher}

class ConsoleScreen(override val matcher: URLMatcher) extends UIScreen with URLActivation {
  override def createUI(): Unit = {
    renderer.backgroundColor := ColorScheme.base3
  }

  override def updateURL(current: URL): Option[HistoryStateChange] = None
}
