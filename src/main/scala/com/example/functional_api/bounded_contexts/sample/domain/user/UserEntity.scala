package com.example.functional_api.bounded_contexts.sample.domain.user

import com.example.functional_api.bounded_contexts.sample.domain.support.AppError

case class UserEntity(id: Id, name: UserName, age: NaturalNumber)

final case class Id(value: Int) extends AnyVal

object Id {
  def apply(value: Int): Option[Id] = Some(value).filter(validate).map(new Id(_))

  def validate(value: Int): Boolean = value > 0
}

final case class UserName(value: String) extends AnyVal

object UserName {
  def apply(value: String): Option[UserName] = Some(value).filter(validate).map(new UserName(_))

  def validate(value: String): Boolean = value.nonEmpty && value.length <= 20
}

case class NaturalNumber(value: Int) extends AnyVal

object NaturalNumber {
  def apply(value: Int): Option[NaturalNumber] =
    Some(value).filter(validate).map(new NaturalNumber(_))

  def validate(value: Int): Boolean = value > 0
}

trait UserError extends AppError
case object UserNotFoundError extends UserError
