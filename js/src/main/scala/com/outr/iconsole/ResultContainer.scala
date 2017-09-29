package com.outr.iconsole

import com.outr.iconsole.result.CommandResult
import io.youi._
import io.youi.component.{Container, DrawableComponent, Text}
import io.youi.font.{Font, GoogleFont}
import io.youi.paint.{Border, Stroke}
import reactify._

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

class ResultContainer(command: Command, result: Future[CommandResult]) extends Container {
  background := ColorScheme.base2
  border := Border(Stroke(Color.Clear, 0.0), 10.0)

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

  result.onComplete {
    case Success(r) => {
      if (r.successful) {
        statusText.value := "Success"
        r.content.position.left := 40.0
        r.content.position.top := statusLabel.position.bottom + 20.0
        size.height := r.content.position.bottom + 10.0
        children += r.content
      } else {
        statusText.value := "Failed"
      }
    }
    case Failure(t) => {
      scribe.error(t)
      statusText.value := s"Error: ${t.getMessage}"
    }
  }

  size.width := ui.width - 10.0
  size.height := statusText.position.bottom + 10.0
  children += commandLabel
  children += commandText
  children += statusLabel
  children += statusText
}