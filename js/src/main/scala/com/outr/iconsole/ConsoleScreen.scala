package com.outr.iconsole

import io.youi.HistoryStateChange
import io.youi.app.screen.{TitledScreen, UIScreen, URLActivation}
import io.youi.net.{URL, URLMatcher}

import scala.concurrent.Future

class ConsoleScreen(override val matcher: URLMatcher,
                    override val title: String = "iConsole") extends UIScreen with URLActivation with TitledScreen {
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

