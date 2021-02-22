package com.example.functional_api.bounded_contexts.sample.infrastructure.support

import com.example.functional_api.bounded_contexts.sample.domain.support.{AppError, UnExpectedError}
import doobie.SqlState

abstract class RepositoryBase {
  def handleSqlException[A](x: Either[SqlState, Either[AppError, A]]): Either[AppError, A] = x match {
    case Right(value) => value
    case Left(_) => Left(UnExpectedError)
  }
}
