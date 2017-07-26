package com.outr.iconsole.example

import com.outr.iconsole.IConsoleApplication
import io.youi.app.ServerApplication
import io.youi.http.path

object IConsoleServer extends ServerApplication with IConsoleApplication {
  handler.matcher(path.exact("/")).page()
}
