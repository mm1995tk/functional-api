package com.example.functional_api.bounded_contexts.sample.presentation.support

import cats.effect.{Async, ContextShift}
import com.example.functional_api.bounded_contexts.sample.usecase.dto.response.ErrorResponseModel
import com.typesafe.config.{Config, ConfigFactory}
import io.circe.generic.encoding.ReprAsObjectEncoder.deriveReprAsObjectEncoder
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.Http4sDsl
import io.circe.syntax._
import io.circe.generic.auto.exportEncoder
import org.http4s.Response

abstract class RoutesBase[F[+_] : Async : ContextShift](dsl: Http4sDsl[F]) {

  import dsl._

  private val config: Config = ConfigFactory.load()

  private val errorResponseModel = (errorTypePath: String) =>
    ErrorResponseModel(config.getInt(s"$errorTypePath.code"), config.getString(s"$errorTypePath.message")).asJson

  val internalServerError: F[Response[F]] = InternalServerError {
    errorResponseModel("error.internalServerError")
  }

  val badRequest: F[Response[F]] = BadRequest {
    errorResponseModel("error.badRequest")
  }
}
