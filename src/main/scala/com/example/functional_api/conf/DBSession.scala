package com.example.functional_api.conf

import cats.effect.{Async, Blocker, ContextShift, IO}
import com.typesafe.config.{Config, ConfigFactory}
import doobie.Transactor
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor.Aux

case object DBSession {
  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)
  val config: Config = ConfigFactory.load()

  def primary[T[_] : Async : ContextShift]: Aux[T, Unit] = Transactor.fromDriverManager[T](
    config.getString("db.driver"),
    config.getString("db.url"),
    config.getString("db.user"),
    config.getString("db.password"),
    Blocker.liftExecutionContext(ExecutionContexts.synchronous) // just for testing
  )
}
