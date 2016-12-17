package com.yardsale.store.validator

import com.yardsale.store.service.StoreActor.DeleteStoreItem
import org.scalatest.FunSuite

class DeleteStoreItemValidatorTest extends FunSuite {

  val validator = new DeleteStoreItemValidator

  test("Test validate command When id is less than 1 Then return 1 violation") {
    assertResult(1)(validator.validate(new DeleteStoreItem(0)).size)
  }

  test("Test validate command When id is more than 0 Then return no violations") {
    assert(validator.validate(new DeleteStoreItem(1)).isEmpty)
  }
}
