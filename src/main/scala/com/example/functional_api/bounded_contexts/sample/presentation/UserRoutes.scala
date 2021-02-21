package com.example.functional_api.bounded_contexts.sample.presentation

import cats.Monad
import cats.effect.{Async, ContextShift}
import cats.implicits.toSemigroupKOps
import com.example.functional_api.bounded_contexts.sample.domain.support.UnExpectedError
import com.example.functional_api.bounded_contexts.sample.domain.user.{Id, UserNotFoundError, UserRepository}
import com.example.functional_api.bounded_contexts.sample.infrastructure.user.UserRepositoryImpl
import com.example.functional_api.bounded_contexts.sample.presentation.support.RoutesBase
import com.example.functional_api.bounded_contexts.sample.usecase.application.user.{GetUserUseCase, GetUsersUseCase}
import doobie.util.transactor.Transactor
import io.circe.generic.auto.exportEncoder
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.Http4sDsl

case class UserRoutes[F[+_] : Async : ContextShift]()(implicit dsl: Http4sDsl[F], xa: Transactor[F]) extends RoutesBase(dsl){

  import dsl._

  implicit lazy val repository: UserRepository[F] = new UserRepositoryImpl[F](xa)

  val userControllers: HttpRoutes[F] =
    index <+> show

  private def index: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root =>
      val getUsersUseCase = GetUsersUseCase()
      Ok(getUsersUseCase())
  }

  private def show: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / IntVar(userId) =>
      val getUserUseCase = GetUserUseCase()
      val responseOpt = for (id <- Id(userId)) yield
        Monad[F].flatMap(getUserUseCase(id)) {
          case Right(res) => Ok(res)
          case Left(err) => err match {
            case UnExpectedError => internalServerError
            case UserNotFoundError => badRequest
          }
        }
      responseOpt getOrElse badRequest
  }

}
