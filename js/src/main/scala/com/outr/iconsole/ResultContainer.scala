package com.outr.iconsole

import com.outr.iconsole.result.CommandResult
import io.youi._
import io.youi.component.draw.{Fill, Group, Stroke}
import io.youi.component.draw.path.Path
import io.youi.component.font.Font
import io.youi.component.{Container, DrawableComponent, Text}
import reactify._

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

class ResultContainer(command: Command, result: Future[CommandResult]) extends Container {
  private val background = new DrawableComponent {
    drawable := Group(
      Path
        .begin
        .roundedRect(0.0, 0.0, size.width, size.height, 10.0)
        .close,
      Fill(ColorScheme.base2),
      Stroke(ColorScheme.base1)
    )
  }

  private val commandLabel = new Text {
    value := "Command: "
    font.file := Font.fromPath("/fonts/OpenSans-Bold.ttf")

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
    font.file := Font.fromPath("/fonts/OpenSans-Bold.ttf")

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

  background.size.width := size.width
  background.size.height := size.height
  size.width := ui.size.width - 10.0
  size.height := statusText.position.bottom + 10.0
  children += background
  children += commandLabel
  children += commandText
  children += statusLabel
  children += statusText
}