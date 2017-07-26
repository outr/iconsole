package com.outr.iconsole

case class Command(module: Option[String], name: String, args: Map[String, String])