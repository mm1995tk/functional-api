package com.example.functional_api.bounded_contexts.sample.usecase.application.user

import cats.Applicative
import com.example.functional_api.bounded_contexts.sample.domain.support.AppError
import com.example.functional_api.bounded_contexts.sample.domain.user.{Id, UserRepository}
import com.example.functional_api.bounded_contexts.sample.usecase.dto.response.UserResponseModel

case class GetUserUseCase[F[+_] : Applicative]() {
  def apply(id: Id)(implicit repository: UserRepository[F]): F[Either[AppError, UserResponseModel]] =
    Applicative[F].map(repository.findById(id))(_.map(UserResponseModel.fromEntity))
}
