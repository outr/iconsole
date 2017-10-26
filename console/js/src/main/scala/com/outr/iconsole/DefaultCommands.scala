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
  def help(command: Option[String] = None): List[String] = command match {
    case Some(cmd) => CommandProcessor.commands.find(_.name.equalsIgnoreCase(cmd)) match {
      case Some(cp) => List(cp.description, s"Syntax: ${cp.syntax}")
      case None => List(s"No command found by name: $cmd")
    }
    case None => List(
      "General Help",
      "Commands can be run by simply typing the name of the command into the input field at the bottom of the screen and hitting enter.",
      "Some commands take arguments. For example, a command called `echo` might take an argument of what to echo back to the screen. This can be represented:",
      "        echo Hello!",
      "If you need to pass a phrase as an argument:",
      """        echo "Hello, World!"""",
      "You can also pass an argument by name:",
      "        echo message=Hello!",
      "To get help for a specific command simply type `hello <command>`. For example:",
      "        help echo",
      "This will give you a description of the command and the syntax available for it. For example:",
      "        Echo's a message to the screen.",
      """        Syntax: echo message: String = "Wahoo!"""",
      """Above you can see the syntax takes one argument `message` of type `String`, but in this case it has a default value for it, "Wahoo!"""",
      "If a command defines default values these arguments are optional. If they are not defined by you, the default will be used.",
      "If you try to execute a command without all the required arguments (and there are no defaults defined), an error will appear telling you what arguments you neglected to provide.",
      "Finally, to get a list of all commands available, you can type `commands`."
    )
  }
}