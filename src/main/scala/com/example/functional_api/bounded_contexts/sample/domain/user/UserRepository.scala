package com.example.functional_api.bounded_contexts.sample.domain.user

import com.example.functional_api.bounded_contexts.sample.domain.support.AppError

trait UserRepository[F[+_]] {
  val findAll: F[List[UserEntity]]
  val findById: Id => F[Either[AppError, UserEntity]]
}
