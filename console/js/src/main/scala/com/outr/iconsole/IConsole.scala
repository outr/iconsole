package com.outr.iconsole

import io.youi.component.TextView
import io.youi.font.{GoogleFont, OpenTypeFont}
import reactify._

import scala.language.experimental.macros

import scala.concurrent.ExecutionContext.Implicits.global

object IConsole {
  init()

  private def init(): Unit = {
    OpenTypeFont.fromURL(GoogleFont.`Open Sans`.regular).foreach(TextView.font.file := _)
    TextView.font.size := 18.0
    TextView.fill := ColorScheme.primary

    CommandProcessor.registerFromObject(None, DefaultCommands)
  }

  def execute(command: Command, includeInHistory: Boolean = true): Unit = CommandInput.process(command, includeInHistory)

  def register(processor: CommandProcessor): Unit = CommandProcessor.register(processor)

  def registerFromObject[T](module: Option[String], obj: T): List[CommandProcessor] = macro ProcessorGenerator.registerFromObject[T]
}