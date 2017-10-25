package com.outr.iconsole

import io.youi.app.{CommunicationManager, YouIApplication}

trait IConsoleApplication extends YouIApplication {
  val iConsoleCommunication: CommunicationManager[IConsoleCommunication] = connectivity.communication[IConsoleCommunication]
}