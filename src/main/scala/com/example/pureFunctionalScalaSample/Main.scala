package com.example.pureFunctionalScalaSample

import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {
  def run(args: List[String]) =
    AppServer.stream[IO].compile.drain.as(ExitCode.Success)
}
