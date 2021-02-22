package com.example.functional_api.bounded_contexts.sample.usecase.application.user

import cats.Applicative
import com.example.functional_api.bounded_contexts.sample.domain.support.AppError
import com.example.functional_api.bounded_contexts.sample.domain.user.{Id, UserNotFoundError, UserRepository}
import com.example.functional_api.bounded_contexts.sample.usecase.dto.response.UserResponseModel

case class GetUserUseCase[F[+_] : Applicative]() {
  def apply(id: Int)(implicit repository: UserRepository[F]): F[Either[AppError, UserResponseModel]] =
    Id(id) match {
      case None => Applicative[F].pure(Left(UserNotFoundError))
      case Some(userId) => Applicative[F].map(repository.findById(userId))(_.map(UserResponseModel.fromEntity))
    }


}
