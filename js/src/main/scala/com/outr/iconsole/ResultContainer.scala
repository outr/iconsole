package com.outr.iconsole

import java.util.Date

import com.outr.iconsole.result.CommandResult
import io.youi._
import io.youi.component.{Container, Text}
import io.youi.font.{Font, GoogleFont}
import io.youi.paint.{Border, Stroke}
import reactify._

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

class ResultContainer(command: Command, result: Future[CommandResult]) extends Container { self =>
  private var completed: Option[Long] = None
  result.onComplete(_ => completed = Some(System.currentTimeMillis()))

  private val commandLabel = new Text {
    value := "Command: "
    font.file := Font.fromURL(GoogleFont.`Open Sans`.`700`)

    position.left := 10.0
    position.top := 10.0
  }

  private val commandText = new Text {
    value := command.text

    position.left := commandLabel.position.right + 10.0
    position.middle := commandLabel.position.middle
  }

  private val statusLabel = new Text {
    value := "Status: "
    font.file := Font.fromURL(GoogleFont.`Open Sans`.`700`)

    position.left := 10.0
    position.top := commandLabel.position.bottom + 10.0
  }

  private val statusText = new Text {
    value := "Running"

    position.left := statusLabel.position.right + 10.0
    position.middle := statusLabel.position.middle
  }

  private val runTimeText = new Text {
    value := new Date(command.started).toString

    position.right := self.size.width - 20.0
    position.middle := statusLabel.position.middle
  }

  private val runTimeLabel = new Text {
    value := "Elapsed: "
    font.file := Font.fromURL(GoogleFont.`Open Sans`.`700`)

    position.right := runTimeText.position.left - 10.0
    position.middle := statusLabel.position.middle
  }

  private val removeLink = new Text {
    visible := false
    value := "X"
    font.file := Font.fromURL(GoogleFont.`Open Sans`.`800`)
    font.size := 20.0

    position.right := self.size.width - 8.0
    position.top := 8.0
    event.click.on(remove())
  }

  event.pointer.overState.attach(removeLink.visible := _)

  result.onComplete {
    case Success(r) => {
      if (r.successful) {
        statusText.value := "Success"
      } else {
        statusText.value := "Failed"
      }
      r.content.position.left := 40.0
      r.content.position.top := statusLabel.position.bottom + 10.0
      size.height := r.content.position.bottom + 10.0
      children += r.content
    }
    case Failure(t) => {
      scribe.error(t)
      statusText.value := s"Error: ${t.getMessage}"
    }
  }

  background := ColorScheme.base2
  border := Border(Stroke(ColorScheme.base0, 2.0), 10.0)
  position.left := 10.0
  size.width := ui.width - 20.0
  size.height := statusText.position.bottom + 10.0

  children += commandLabel
  children += commandText
  children += statusLabel
  children += statusText
  children += runTimeLabel
  children += runTimeText
  children += removeLink

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