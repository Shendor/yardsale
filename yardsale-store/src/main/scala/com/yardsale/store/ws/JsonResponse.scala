package com.yardsale.store.ws

import com.yardsale.store.validator.Violation

case class JsonResponse[T](response: T, violations: Iterable[Violation]) {

  def hasViolations: Boolean = violations.nonEmpty
}