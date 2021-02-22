package com.example.functional_api.bounded_contexts.sample.presentation

import cats.Monad
import cats.effect.{Async, ContextShift}
import cats.implicits.toSemigroupKOps
import com.example.functional_api.bounded_contexts.sample.domain.support.{AppError, UnExpectedError}
import com.example.functional_api.bounded_contexts.sample.domain.user.{UserNotFoundError, UserRepository}
import com.example.functional_api.bounded_contexts.sample.infrastructure.user.UserRepositoryImpl
import com.example.functional_api.bounded_contexts.sample.presentation.support.RoutesBase
import com.example.functional_api.bounded_contexts.sample.usecase.application.user.{GetUserUseCase, GetUsersUseCase}
import doobie.util.transactor.Transactor
import io.circe.generic.auto.exportEncoder
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, Response}
import org.log4s.getLogger

case class UserRoutes[F[+_] : Async : ContextShift]()
    (implicit dsl: Http4sDsl[F], xa: Transactor[F]) extends RoutesBase(dsl) {

  import dsl._

  implicit override def handleError(err: AppError): F[Response[F]] = {
    getLogger.error(err.toString)
    err match {
      case UnExpectedError => internalServerError
      case UserNotFoundError => badRequest
    }
  }

  implicit lazy val repository: UserRepository[F] = new UserRepositoryImpl[F](xa)

  val userControllers: HttpRoutes[F] =
    index <+> show

  private def index(implicit handleError: AppError => F[Response[F]]): HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root =>
      val getUsersUseCase = GetUsersUseCase()
      Monad[F].flatMap(getUsersUseCase()) {
        case Right(res) => Ok(res)
        case Left(err) => handleError(err)
      }
  }

  private def show(implicit handleError: AppError => F[Response[F]]): HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / IntVar(userId) =>
      val getUserUseCase = GetUserUseCase()
      Monad[F].flatMap(getUserUseCase(userId)) {
        case Right(res) => Ok(res)
        case Left(err) => handleError(err)
      }
  }

}
