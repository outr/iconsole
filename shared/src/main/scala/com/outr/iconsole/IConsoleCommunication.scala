package com.outr.iconsole

import io.youi.communication.{Communication, server}

import scala.concurrent.Future

trait IConsoleCommunication extends Communication {
  /**
    * Client calls this to notify the server that the command was executed.
    *
    * The primary purpose of this is for logging and command history.
    *
    * @param command the command that was executed
    */
  @server def commandExecuted(command: Command): Future[Unit]

  /**
    * Client calls this when it first loads to get the recent history.
    *
    * @return a Vector of the most recent commands invoked
    */
  @server def loadHistory(): Future[Vector[Command]]

  /**
    * Clears the command history on the server.
    */
  @server def clearHistory(): Future[Unit]
}