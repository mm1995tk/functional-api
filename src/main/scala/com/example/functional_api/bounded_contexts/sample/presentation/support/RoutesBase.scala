package com.example.functional_api.bounded_contexts.sample.presentation.support

import cats.effect.{Async, ContextShift}
import com.example.functional_api.bounded_contexts.sample.domain.support.AppError
import com.example.functional_api.bounded_contexts.sample.usecase.dto.response.ErrorResponseModel
import com.typesafe.config.{Config, ConfigFactory}
import io.circe.generic.encoding.ReprAsObjectEncoder.deriveReprAsObjectEncoder
import io.circe.syntax._
import org.http4s.Response
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.Http4sDsl
import io.circe.generic.auto.exportEncoder

abstract class RoutesBase[F[+_] : Async : ContextShift](dsl: Http4sDsl[F]) {

  abstract def handleError(err: AppError): F[Response[F]]

  import dsl._

  lazy private val config: Config = ConfigFactory.load()

  private val errorResponseModel = (errorTypePath: String) =>
    ErrorResponseModel(
      config.getInt(s"error.$errorTypePath.code"),
      config.getString(s"error.$errorTypePath.message")
    ).asJson

  val internalServerError: F[Response[F]] = InternalServerError {
    errorResponseModel("internalServerError")
  }

  val badRequest: F[Response[F]] = BadRequest {
    errorResponseModel("badRequest")
  }
}
