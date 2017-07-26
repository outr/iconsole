package com.outr.iconsole

import io.youi.http.Connection
import reactify.Var

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait ServerIConsoleCommunication extends IConsoleCommunication {
  /**
    * Determines the logged in user for this connection if there is one. Used for command history logging and retention.
    */
  def username: Option[String] = ServerIConsoleCommunication.usernameForConnection()(connection)

  override def commandExecuted(command: Command): Future[Unit] = {
    DatabaseController.addCommandHistory(username, command)
  }

  override def loadHistory(): Future[Vector[Command]] = {
    DatabaseController.commandHistory(username).map(_.map(_.command).toVector)
  }

  override def clearHistory(): Future[Unit] = DatabaseController.clearCommandHistory(username).map(_ => ())
}

object ServerIConsoleCommunication {
  val usernameForConnection: Var[(Connection) => Option[String]] = Var(_ => None)
}