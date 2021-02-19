package com.example.functional_api.bounded_contexts.sample.presentation

import cats.Monad
import cats.effect.{Async, ContextShift}
import cats.implicits.toSemigroupKOps
import com.example.functional_api.bounded_contexts.sample.domain.support.UnExpectedError
import com.example.functional_api.bounded_contexts.sample.domain.user.{Id, UserNotFoundError, UserRepository}
import com.example.functional_api.bounded_contexts.sample.infrastructure.user.UserRepositoryImpl
import com.example.functional_api.bounded_contexts.sample.usecase.application.user.{GetUserUseCase, GetUsersUseCase}
import com.example.functional_api.bounded_contexts.sample.usecase.dto.response.ErrorResponseModel
import doobie.util.transactor.Transactor
import io.circe.generic.auto.exportEncoder
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.Http4sDsl

case class UserRoutes[F[+_] : Async : ContextShift](dsl: Http4sDsl[F], xa: Transactor[F]) {

  import dsl._

  implicit lazy val repository: UserRepository[F] = new UserRepositoryImpl[F](xa)

  def userRoutes: HttpRoutes[F] =
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
          case Left(err) => err match {
            case UnExpectedError => InternalServerError(ErrorResponseModel(500, "internal error"))
            case UserNotFoundError => BadRequest(ErrorResponseModel(400, "not found"))
          }
          case Right(res) => Ok(res)
        }
      responseOpt getOrElse BadRequest()
  }

}
