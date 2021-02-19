package com.example.functional_api

import cats.effect.{Async, ConcurrentEffect, ContextShift, ExitCode, IO, IOApp, Timer}
import com.example.functional_api.bounded_contexts.sample.presentation.UserRoutes
import com.example.functional_api.conf.DBSession
import fs2.Stream
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger

import scala.concurrent.ExecutionContext.global

object AppServer extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    AppServer.stream[IO].compile.drain.as(ExitCode.Success)

  def stream[F[+_] : ConcurrentEffect : Async : ContextShift](implicit T: Timer[F]): Stream[F, Nothing] = {
    val dsl = new Http4sDsl[F] {}
    val xa = DBSession.primary

    val httpApp = Router(
      "users" -> UserRoutes[F](dsl, xa).userRoutes
    ).orNotFound

    val finalHttpApp = Logger.httpApp(logHeaders = true, logBody = true)(httpApp)

    BlazeServerBuilder[F](global)
        .bindHttp(9000, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .serve
        .drain
  }
}