package com.outr.iconsole.example

import com.outr.iconsole.{description, shortDescription}
import io.youi.ImageMode
import io.youi.component.ImageView

import scala.concurrent.{Future, Promise}
import org.scalajs.dom._

object ExampleCommands {
  def echo(message: String = "Wahoo!"): String = {
    s"Message: $message"
  }

  def alert(): Unit = scribe.info("Alert!")

  @shortDescription("Displays a cute picture of a puppy.")
  @description("Displays a very cute picture of a puppy as an image on the screen.")
  def puppy(): ImageView = new ImageView("https://lh3.googleusercontent.com/kyAkKnaOJHkLBf5huWaJHbqiYbG-h7ThtB3ZCY_SYpU_-AB1RToY1FX8iUzhr6W3cg=h310")

  def delayed(): Future[String] = {
    val promise = Promise[String]
    window.setTimeout(() => {
      promise.success("Delayed successfully!")
    }, 2500.0)
    promise.future
  }
}