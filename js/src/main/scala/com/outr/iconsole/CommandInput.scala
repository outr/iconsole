package com.outr.iconsole

import io.youi.component.{Container, DrawableComponent, HTMLComponent, Text}
import io.youi.component.draw.path.Path
import io.youi.component.draw.{Fill, Group}
import io.youi._
import io.youi.component.font.Font
import io.youi.hypertext.TextInput
import reactify._

object CommandInput extends Container {
  object background extends DrawableComponent {
    drawable := Group(
      Path.begin.roundedRect(0.0, 0.0, ui.size.width - 10.0, 40.0, 5.0).close,
      Fill(ColorScheme.base1)
    )
  }

  object text extends HTMLComponent[TextInput](new TextInput) {
    component.color := ColorScheme.primary
    component.font.size := 24.0
    component.font.family := "sans-serif"
    component.backgroundColor := Color.Clear
    component.border.size := Some(0.0)
    component.element.style.outline = "none"
    component.size.width := ui.size.width - 50.0
    position.left := 15.0
    position.top := 6.0
  }

  children += background
  children += text

  def value: Var[String] = text.component.value
}