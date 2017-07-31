package com.outr.iconsole

import io.youi.component.Text
import io.youi.component.font.Font
import reactify._

object IConsole {
  Text.font.file := Font.fromPath("/fonts/OpenSans-Regular.ttf")
  Text.font.size := 18.0
  Text.fill := ColorScheme.primary
}