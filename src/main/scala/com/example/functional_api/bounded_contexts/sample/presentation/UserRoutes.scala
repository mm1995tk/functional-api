package com.example.functional_api.bounded_contexts.sample.presentation

import cats.Monad
import cats.effect.{Async, ContextShift}
import cats.implicits.toSemigroupKOps
import com.example.functional_api.bounded_contexts.sample.domain.support.{AppError, UnExpectedError}
import com.example.functional_api.bounded_contexts.sample.domain.user.{UserNotFoundError, UserRepository}
import com.example.functional_api.bounded_contexts.sample.infrastructure.user.UserRepositoryImpl
import com.example.functional_api.bounded_contexts.sample.presentation.support.{RoutesBase, RoutesInterface}
import com.example.functional_api.bounded_contexts.sample.usecase.application.user.{GetUserUseCase, GetUsersUseCase}
import doobie.util.transactor.Transactor
import io.circe.generic.auto.exportEncoder
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.Http4sDsl
import org.log4s.{Logger, getLogger}
import org.http4s.{HttpRoutes, Response}

case class UserRoutes[F[+_] : Async : ContextShift]()
    (implicit dsl: Http4sDsl[F], xa: Transactor[F]) extends RoutesBase(dsl) with RoutesInterface[F] {

  import dsl._

  implicit override def handleError(err: AppError): F[Response[F]] = err match {
    case UnExpectedError => internalServerError
    case UserNotFoundError => badRequest
  }

  implicit lazy val repository: UserRepository[F] = new UserRepositoryImpl[F](xa)

  implicit lazy val logger: Logger = getLogger

  val userControllers: HttpRoutes[F] =
    index <+> show

  private def index: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root =>
      val getUsersUseCase = GetUsersUseCase()
      Ok(getUsersUseCase())
  }

  private def show(implicit handleError: AppError => F[Response[F]], logger: Logger): HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / IntVar(userId) =>
      val getUserUseCase = GetUserUseCase()
      Monad[F].flatMap(getUserUseCase(userId)) {
        case Right(res) => Ok(res)
        case Left(err) =>
          logger.info(err.toString)
          handleError(err)
      }
  }

}
