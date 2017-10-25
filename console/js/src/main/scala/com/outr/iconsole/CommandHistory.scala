package com.outr.iconsole

import io.youi.net.URL
import org.scalajs.dom.window

object CommandHistory {
  var MaxBacklog: Int = 1000

  private val storage = window.localStorage
  private var index = -1
  private var backlog: Vector[String] = load()

  def add(command: String): Unit = if (!backlog.headOption.contains(command)) {     // Avoid duplicates in a row
    backlog = (command +: backlog).take(MaxBacklog)
    save()
  }

  def previous(): Option[String] = if (index < backlog.length - 1) {
    index += 1
    Some(backlog(index))
  } else {
    None
  }

  def next(): Option[String] = {
    index = math.max(-1, index - 1)
    if (index >= 0) {
      Some(backlog(index))
    } else {
      None
    }
  }

  def clear(): Unit = {
    storage.clear()
    backlog = Vector.empty
    index = -1
  }

  private def load(): Vector[String] = Option(storage.getItem("backlog")).map(_.split('&').map(URL.decode).toVector).getOrElse(Vector.empty)

  private def save(): Unit = {
    val encoded = backlog.map(URL.encode).mkString("&")
    storage.setItem("backlog", encoded)
  }
}