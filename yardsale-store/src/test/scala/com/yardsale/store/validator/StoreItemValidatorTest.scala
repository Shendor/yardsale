package com.yardsale.store.validator

import com.yardsale.store.domain.StoreItem
import org.scalatest.{FunSuite, Matchers}

class StoreItemValidatorTest extends FunSuite
  with Matchers {

  val itemValidator = new StoreItemValidator

  test("Test validate store item When it's null Then return violation") {
    val violations: Seq[Violation] = itemValidator.validate(null)

    assertResult(1)(violations.size)
    assertResult("item")(violations.head.field)
    assertResult("item.invalid")(violations.head.message)
  }

  test("Test validate store item When it has all invalid fields Then return violations") {
    val item: StoreItem = new StoreItem
    item.id = -1
    item.price = -1
    val errorMessages = itemValidator.validate(item) map { i => i.message }

    assertResult(3)(errorMessages.size)
    assert(errorMessages contains "post_store_item.id.invalid")
    assert(errorMessages contains "post_store_item.name.invalid")
    assert(errorMessages contains "post_store_item.price.invalid")
  }

  test("Test validate store item When it has all valid fields Then return no violations") {
    val item: StoreItem = new StoreItem
    item.name = "name"

    val violations = itemValidator.validate(item)

    assert(violations.isEmpty)
  }

  test("Test validate store item When it has name less than 4 chars Then return violations") {
    val item: StoreItem = new StoreItem
    item.name = "123"

    val violations = itemValidator.validate(item) map { i => i.field }

    assertResult(1)(violations.size)
    assertResult("name")(violations.head)
  }
}
