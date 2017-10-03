package com.outr.iconsole

import com.outr.iconsole.result.{CommandResult, TextResult}
import io.youi.component.{Container, HTMLComponent}
import io.youi._
import io.youi.hypertext.TextInput
import io.youi.paint.{Border, Stroke}
import reactify._

import scala.concurrent.Future

object CommandInput extends Container {
  background := ColorScheme.base1
  border := Border(Stroke(Color.Clear, lineWidth = 0.0), 5.0)

  object text extends HTMLComponent[TextInput](new TextInput) {
    component.color := ColorScheme.primary
    component.font.size := 24.0
    component.font.family := "sans-serif"
    component.backgroundColor := Color.Clear
    component.border.size := Some(0.0)
    component.element.style.outline = "none"
    component.size.width := ui.width - 20.0
    size.height := 30.0
    position.x := 8.0
    position.y := 1.0
  }

  children += text

  ui.event.key.down.attach { evt =>
    if (evt.key == Key.Enter) {
      processValue()
    } else if (evt.key == Key.ArrowUp) {
      evt.stopPropagation()
      evt.preventDefault()
      CommandHistory.previous().foreach(value := _)
    } else if (evt.key == Key.ArrowDown) {
      evt.stopPropagation()
      evt.preventDefault()
      value := CommandHistory.next().getOrElse("")
    } else if (evt.key == Key.Tab) {
      evt.stopPropagation()
    }
  }

  def processValue(commandString: String = value(),
              autoClear: Boolean = true,
              includeInHistory: Boolean = true): Unit = Command.parse(commandString).foreach { command =>
    // TODO: support active module
    process(command, includeInHistory)
    if (autoClear) clear()
  }

  def process(command: Command, includeInHistory: Boolean = true): Unit = CommandProcessor.process(None, command) match {
    case Some(commandResult) => {
      if (includeInHistory) CommandHistory.add(command.text)
      val resultContainer = new ResultContainer(command, commandResult)
      resultContainer.position.center := ui.position.center()
      ConsoleResults.children += resultContainer
    }
    case None => {
      val result = new TextResult("Command not found!")
      val resultContainer = new ResultContainer(command, Future.successful(CommandResult(successful = false, content = result)))
      ConsoleResults.children += resultContainer
    }
  }

  def value: Var[String] = text.component.value

  def clear(): Unit = value := ""
}