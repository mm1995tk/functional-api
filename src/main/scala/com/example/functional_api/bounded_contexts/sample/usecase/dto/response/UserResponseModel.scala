package com.example.functional_api.bounded_contexts.sample.usecase.dto.response

import com.example.functional_api.bounded_contexts.sample.domain.user.UserEntity

case class UserResponseModel(id: Int, name: String, age: Int)

object UserResponseModel {

  def fromEntity(entity: UserEntity): UserResponseModel =
    UserResponseModel(
      entity.id.value,
      entity.name.value,
      entity.age.value
    )
}
