package com.outr.iconsole

import io.youi.component.{Container, TextView}
import io.youi.font.{Font, GoogleFont, OpenTypeFont}
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
        val name: TextView = new TextView {
          value := s"${cmd.name}: "
          OpenTypeFont.fromURL(GoogleFont.`Open Sans`.`600`).foreach(font.file := _)
        }
        val description: TextView = new TextView {
          value := cmd.shortDescription
          OpenTypeFont.fromURL(GoogleFont.`Open Sans`.regular).foreach(font.file := _)
        }
        description.position.left := name.position.right + 10
        children ++= Seq(name, description)
      }
    }
  }

  def clear(): Unit = ConsoleResults.children := Vector.empty

  def clearHistory(): Unit = CommandHistory.clear()
}