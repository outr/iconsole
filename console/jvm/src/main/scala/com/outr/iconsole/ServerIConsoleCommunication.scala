package com.outr.iconsole

import io.youi.http.Connection
import reactify.Var

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait ServerIConsoleCommunication extends IConsoleCommunication {
}

object ServerIConsoleCommunication {
  val usernameForConnection: Var[(Connection) => Option[String]] = Var(_ => None)
}