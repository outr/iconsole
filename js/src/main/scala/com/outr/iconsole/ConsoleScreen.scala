package com.outr.iconsole

import com.outr.iconsole.result.{CommandResult, TextResult}
import io.youi.{HistoryStateChange, Key, ui}
import io.youi.app.screen.{UIScreen, URLActivation}
import io.youi.net.{URL, URLMatcher}

import scala.concurrent.Future

class ConsoleScreen(override val matcher: URLMatcher) extends UIScreen with URLActivation {
  override def createUI(): Future[Unit] = {
    container.background := ColorScheme.base3

    container.children += ConsoleResults
    CommandInput.position.center := container.position.center
    CommandInput.position.bottom := container.position.bottom - 5.0
    container.children += CommandInput

    Future.successful(())
  }

  override def updateURL(current: URL): Option[HistoryStateChange] = None
}

