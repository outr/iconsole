package com.outr.iconsole.example

import com.outr.iconsole.IConsoleApplication
import io.youi.app.ServerApplication
import io.youi.http.path
import io.youi.server.handler.CachingManager

object IConsoleServer extends ServerApplication with IConsoleApplication {
  override def main(args: Array[String]): Unit = start(args)

  protected override def run(): Unit = {
    handler.matcher(path.exact("/")).page()

    handler.caching(CachingManager.LastModified()).classLoader("", (path: String) => s"content$path")

    super.run()
  }
}
