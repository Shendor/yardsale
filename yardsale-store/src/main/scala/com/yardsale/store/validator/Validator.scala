package com.yardsale.store.validator

import com.yardsale.store.service.Command

trait Validator[T] {

  def validate(item: T): Seq[Violation] = {
    val violations: Seq[Violation] = verify(item == null)("item", "item.invalid")
    if (violations.isEmpty)
      validateFields(item)
    else violations
  }

  protected def validateFields(storeItem: T): Seq[Violation]

  protected def verify(isNotValid: Boolean)(fieldName: String, errorCode: String): Seq[Violation] = isNotValid match {
    case true => List(Violation(fieldName, errorCode))
    case _ => List.empty
  }

}

object Validator {

  case class CommandViolated(command : Command, violations: Seq[Violation])

}
