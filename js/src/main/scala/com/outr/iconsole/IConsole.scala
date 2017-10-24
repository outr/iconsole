package com.outr.iconsole

import io.youi.component.TextView
import io.youi.font.{GoogleFont, OpenTypeFont}
import reactify._

import scala.concurrent.ExecutionContext.Implicits.global

object IConsole {
  OpenTypeFont.fromURL(GoogleFont.`Open Sans`.regular).foreach(TextView.font.file := _)
  TextView.font.size := 18.0
  TextView.fill := ColorScheme.primary
}