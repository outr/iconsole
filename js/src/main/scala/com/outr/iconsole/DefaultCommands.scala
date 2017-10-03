package com.outr.iconsole

import io.youi.component.{Container, Text}
import io.youi.font.{Font, GoogleFont}
import reactify._

object DefaultCommands {
  def init(): Unit = {
    CommandProcessor("commands", description = "Lists all available commands.")(_ => commands())
    CommandProcessor("clear", description = "Clears the screen.")(_ => clear())
    CommandProcessor("clearHistory", description = "Clears the command history backlog.")(_ => clearHistory())
  }

  def commands(): List[Container] = {
    CommandProcessor.commands.map { cmd =>
      new Container {
        val name: Text = new Text {
          value := s"${cmd.name}: "
          font.file := Font.fromURL(GoogleFont.`Open Sans`.`600`)
        }
        val description: Text = new Text {
          value := cmd.shortDescription
          font.file := Font.fromURL(GoogleFont.`Open Sans`.regular)
        }
        description.position.left := name.position.right + 10
        children ++= Seq(name, description)
      }
    }
  }

  def clear(): Unit = ConsoleResults.children := Vector.empty

  def clearHistory(): Unit = CommandHistory.clear()
}