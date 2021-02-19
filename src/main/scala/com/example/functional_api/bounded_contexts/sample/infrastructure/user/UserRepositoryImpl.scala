package com.example.functional_api.bounded_contexts.sample.infrastructure.user

import cats.effect._
import com.example.functional_api.bounded_contexts.sample.domain.support.AppError
import com.example.functional_api.bounded_contexts.sample.domain.user._
import doobie.implicits._
import doobie.util.transactor.Transactor
import fs2.Stream

class UserRepositoryImpl[F[+_] : Async : ContextShift](xa: Transactor[F]) extends UserRepository[F] {

  val findAll: F[List[UserEntity]] =
    sql"select * from users"
        .query[UserModel]
        .stream
        .flatMap(model => Stream.emits(List(model.toEntity).flatten))
        .compile
        .toList
        //        .map(_.sequence.toRight())
        .transact(xa)

  val findById: Id => F[Either[AppError, UserEntity]] = (id: Id) =>
    sql"select * from users where id = ${id.value}"
        .query[UserModel]
        .option
        .map(_.flatMap(_.toEntity).toRight(UserNotFoundError))
        .transact(xa)

  case class UserModel(id: Int, name: String, age: Int) {
    def toEntity: Option[UserEntity] = {
      for {
        entityId <- Id(id)
        entityName <- UserName(name)
        entityAge <- NaturalNumber(age)
      } yield UserEntity(entityId, entityName, entityAge)
    }
  }

}
