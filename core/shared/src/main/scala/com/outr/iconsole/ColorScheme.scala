package com.outr.iconsole

import io.youi.Color
import reactify.Var

case class ColorScheme(base0: Color,
                       base1: Color,
                       base2: Color,
                       base3: Color,
                       yellow: Color,
                       orange: Color,
                       red: Color,
                       magenta: Color,
                       violet: Color,
                       blue: Color,
                       cyan: Color,
                       green: Color,
                       primary: Color)

object ColorScheme extends Var[ColorScheme](() => Solarized.Dark, distinct = true, cache = true)