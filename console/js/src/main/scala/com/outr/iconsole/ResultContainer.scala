package com.outr.iconsole

import java.util.Date

import com.outr.iconsole.result.CommandResult
import io.youi._
import io.youi.component.{Container, TextView}
import io.youi.font.{Font, GoogleFont, OpenTypeFont}
import io.youi.paint.{Border, Stroke}
import reactify._

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

class ResultContainer(val command: Command,
                      val result: Future[CommandResult],
                      val identifier: String = Unique(length = 6, characters = Unique.Readable.toLowerCase)) extends Container { self =>
  private var completed: Option[Long] = None
  result.onComplete(_ => completed = Some(System.currentTimeMillis()))

  private val commandLabel = new TextView {
    value := "Command: "
    OpenTypeFont.fromURL(GoogleFont.`Open Sans`.`700`).foreach(font.file := _)

    position.left := 10.0
    position.top := 10.0
  }

  private val commandText = new TextView {
    value := command.text

    position.left := commandLabel.position.right + 5.0
    position.middle := commandLabel.position.middle
  }

  private val statusLabel = new TextView {
    value := "Status: "
    OpenTypeFont.fromURL(GoogleFont.`Open Sans`.`700`).foreach(font.file := _)

    position.left := 10.0
    position.top := commandLabel.position.bottom + 10.0
  }

  private val statusText = new TextView {
    value := "Running"
    fill := ColorScheme.blue

    position.left := statusLabel.position.right + 5.0
    position.middle := statusLabel.position.middle
  }

  private val idText = new TextView {
    value := identifier

    position.right := self.size.width - 40.0
    position.middle := commandLabel.position.middle
  }

  private val idLabel = new TextView {
    value := "ID: "
    OpenTypeFont.fromURL(GoogleFont.`Open Sans`.`700`).foreach(font.file := _)

    position.right := idText.position.left - 5.0
    position.middle := commandLabel.position.middle
  }

  private val runTimeText = new TextView {
    value := new Date(command.started).toString

    position.right := self.size.width - 40.0
    position.middle := statusLabel.position.middle
  }

  private val runTimeLabel = new TextView {
    value := "Elapsed: "
    OpenTypeFont.fromURL(GoogleFont.`Open Sans`.`700`).foreach(font.file := _)

    position.right := runTimeText.position.left - 5.0
    position.middle := statusLabel.position.middle
  }

  private val removeLink = new TextView {
    visible := false
    value := "X"
    fill := ColorScheme.red
    OpenTypeFont.fromURL(GoogleFont.`Open Sans`.`800`).foreach(font.file := _)
    font.size := 20.0

    position.right := self.size.width - 8.0
    position.top := 8.0
    event.pointer.overState.attach {
      case true => fill := Color.White
      case false => fill := ColorScheme.red
    }
    event.click.on(remove())
  }

  event.pointer.overState.attach(removeLink.visible := _)

  result.onComplete {
    case Success(r) => {
      if (r.successful) {
        statusText.value := "Success"
        statusText.fill := ColorScheme.green
      } else {
        statusText.value := "Failed"
        statusText.fill := ColorScheme.red
      }
      r.content.position.left := 40.0
      r.content.position.top := statusLabel.position.bottom + 10.0
      size.height := r.content.position.bottom + 10.0
      children += r.content
    }
    case Failure(t) => {
      scribe.error(t)
      statusText.value := s"Error: ${t.getMessage}"
      statusText.fill := ColorScheme.red
    }
  }

  background := ColorScheme.base2
  border := Border(Stroke(ColorScheme.base0, lineWidth = 2.0), 10.0)
  position.left := 10.0
  size.width := ui.width - 20.0
  size.height := statusText.position.bottom + 10.0

  children ++= Seq(
    idLabel, idText,
    commandLabel, commandText,
    statusLabel, statusText,
    runTimeLabel, runTimeText,
    removeLink
  )

  private var elapsing = true

  override def update(delta: Double): Unit = {
    super.update(delta)

    if (elapsing) formatElapsed()
  }

  def formatElapsed(): Unit = {
    if (result.isCompleted) {
      runTimeLabel.value := "Completed in "

      elapsing = false
    }
    val now = completed.getOrElse(System.currentTimeMillis())
    val elapsed = (now - command.started) / 1000.0
    val seconds = f"$elapsed%.3f seconds"
    runTimeText.value := seconds
  }

  def remove(): Unit = {
    ConsoleResults.children -= this
  }
}