package com.example.functional_api.bounded_contexts.sample.usecase.application.user

import cats.Applicative
import com.example.functional_api.bounded_contexts.sample.domain.support.AppError
import com.example.functional_api.bounded_contexts.sample.domain.user.UserRepository
import com.example.functional_api.bounded_contexts.sample.usecase.dto.response.UserResponseModel

case class GetUsersUseCase[F[+_] : Applicative]() {
  def apply()(implicit repository: UserRepository[F]): F[Either[AppError, List[UserResponseModel]]] =
    Applicative[F].map(repository.findAll)(_.map(_.map(UserResponseModel.fromEntity)))
}
