package com.outr.iconsole

import io.youi.component.Text
import io.youi.font.{Font, GoogleFont}
import reactify._

object IConsole {
  Text.font.file := Font.fromURL(GoogleFont.`Open Sans`.regular)
  Text.font.size := 18.0
  Text.fill := ColorScheme.primary
}