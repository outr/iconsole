package com.outr.iconsole

import io.youi.component.TypedContainer
import io.youi.component.mixins.ScrollSupport
import io.youi.layout.VerticalLayout
import io.youi.ui

object ConsoleResults extends TypedContainer[ResultContainer] with ScrollSupport {
  layoutManager := new VerticalLayout(10.0, fromTop = true)
  position.left := 0.0
  position.bottom := ui.height - CommandInput.size.height - 10.0
  size.width := ui.width
  size.height := math.min(ui.height - CommandInput.size.height - 10.0, size.measured.height)
  size.measured.height.on {
    scroll.vertical.bottom()
  }
}
