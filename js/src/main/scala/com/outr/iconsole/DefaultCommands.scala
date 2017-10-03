package com.outr.iconsole

import io.youi.component.{Container, Text}
import io.youi.font.{Font, GoogleFont}
import reactify._

object DefaultCommands {
  def init(): Unit = {
    CommandProcessor("commands", description = "Lists all available commands.")(_ => commands())
    CommandProcessor("clear", description = "Clears the screen.")(_ => clear())
  }

  def commands(): List[Container] = {
    CommandProcessor.commands.map { cmd =>
      new Container {
        val name = new Text {
          value := s"${cmd.name}: "
          font.file := Font.fromURL(GoogleFont.`Open Sans`.`600`)
        }
        val description = new Text {
          value := cmd.shortDescription
          font.file := Font.fromURL(GoogleFont.`Open Sans`.regular)
        }
        description.position.left := name.position.right + 10
        children ++= Seq(name, description)
      }
    }
  }

  def clear(): Unit = {
    ConsoleResults.children := Vector.empty
  }
}