package com.example.functional_api.bounded_contexts.sample.domain.support

trait AppError extends Throwable

case object UnExpectedError extends AppError
