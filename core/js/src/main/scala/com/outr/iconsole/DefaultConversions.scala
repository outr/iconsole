package com.outr.iconsole

import scala.language.implicitConversions

object DefaultConversions {
  implicit def string2Option(s: String): Option[String] = Option(s)
}
