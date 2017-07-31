package com.outr.iconsole

import com.outr.iconsole.result.CommandResult
import io.youi.component.draw.{Fill, Group}
import io.youi.component.draw.path.Path
import io.youi.component.font.Font
import io.youi.component.{Container, DrawableComponent, Text}
import reactify._

import scala.concurrent.Future

class ResultContainer(command: Command, result: Future[CommandResult]) extends Container {
  private val background = new DrawableComponent {
    drawable := Group(
      Path
        .begin
        .roundedRect(0.0, 0.0, size.width, size.height, 10.0)
        .close,
      Fill(ColorScheme.base1)
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

  background.size.width := size.width
  background.size.height := size.height
  size.width := size.measured.width + 10.0
  size.height := size.measured.height + 10.0
  children += background
  children += commandLabel
  children += commandText
}