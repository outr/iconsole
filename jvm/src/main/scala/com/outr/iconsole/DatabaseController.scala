package com.outr.iconsole

import com.outr.arango._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import io.circe.generic.auto._

object DatabaseController {
  private lazy val arango: Arango = new Arango()
  private lazy val session: ArangoSession = Await.result(arango.session(), 15.seconds)
  private lazy val db: ArangoDB = session.db()
  private lazy val commandHistory: ArangoCollection = db.collection("commandHistory")

  private lazy val initialization: Future[Unit] = commandHistory.exists().flatMap {
    case Some(_) => Future.successful(()) // Already exists
    case None => commandHistory.create().map { result =>
      assert(!result.error, s"Error while creating commandHistory. Code: ${result.code}")
    }
  }

  def init(): Unit = Await.result(initialization, 15.seconds)

  def dispose(): Unit = arango.dispose()

  def addCommandHistory(username: Option[String], command: Command): Future[Unit] = initialization.flatMap { _ =>
    commandHistory.document.create(CommandHistory(username, command)).map(_ => ())
  }

  def clearCommandHistory(username: Option[String]): Future[List[CommandHistory]] = initialization.flatMap { _ =>
    val query = username match {
      case Some(u) => aql"FOR command IN commandHistory FILTER command.username == $u REMOVE command in commandHistory RETURN command"
      case None => aql"FOR command IN commandHistory REMOVE command in commandHistory RETURN command"
    }
    db.cursor[CommandHistory](query).map { result =>
      result.result
    }
  }

  def commandHistory(username: Option[String] = None, limit: Int = 50): Future[List[CommandHistory]] = initialization.flatMap { _ =>
    val query = username match {
      case Some(u) => aql"FOR command IN commandHistory FILTER command.username == $u SORT command.timestamp DESC LIMIT $limit RETURN command"
      case None => aql"FOR command IN commandHistory SORT command.timestamp DESC LIMIT $limit RETURN command"
    }
    db.cursor[CommandHistory](query).map { result =>
      result.result
    }
  }
}

case class CommandHistory(username: Option[String],
                          command: Command,
                          timestamp: Long = System.currentTimeMillis(),
                          _id: Option[String] = None,
                          _key: Option[String] = None,
                          _rev: Option[String] = None) extends DocumentOption