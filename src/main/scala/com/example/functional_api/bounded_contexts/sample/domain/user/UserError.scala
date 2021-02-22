package com.example.functional_api.bounded_contexts.sample.domain.user

import com.example.functional_api.bounded_contexts.sample.domain.support.AppError

trait UserError extends AppError

case object UserNotFoundError extends UserError