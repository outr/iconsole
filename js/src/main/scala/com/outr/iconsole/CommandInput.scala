package com.outr.iconsole

import io.youi.component.{Container, HTMLComponent}
import io.youi._
import io.youi.hypertext.TextInput
import io.youi.paint.{Border, Stroke}
import reactify._

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

  def value: Var[String] = text.component.value
}