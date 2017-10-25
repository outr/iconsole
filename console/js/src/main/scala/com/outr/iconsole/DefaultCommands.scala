package com.outr.iconsole

import io.youi.component.{Container, TextView}
import io.youi.font.{GoogleFont, OpenTypeFont}
import reactify._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object DefaultCommands {
  @description("Lists all available commands.")
  def commands(): Future[List[Container]] = for {
    openSans600 <- OpenTypeFont.fromURL(GoogleFont.`Open Sans`.`600`)
    openSansRegular <- OpenTypeFont.fromURL(GoogleFont.`Open Sans`.regular)
  } yield {
    CommandProcessor.commands.map { cmd =>
      new Container {
        val name: TextView = new TextView {
          value := s"${cmd.name}: "
          font.file := openSans600
        }
        val description: TextView = new TextView {
          value := cmd.shortDescription
          font.file := openSansRegular
        }
        description.position.left := name.position.right + 10
        children ++= Seq(name, description)
      }
    }
  }

  @description("Clears the screen.")
  def clear(): Unit = ConsoleResults.children := Vector.empty

  @description("Clears the command history backlog.")
  def clearHistory(): Unit = CommandHistory.clear()

  @description("General or specific help for a command.")
  def help(command: Option[String] = None): String = command match {
    case Some(cmd) => s"Help for $cmd"
    case None => "General Help"
  }
}