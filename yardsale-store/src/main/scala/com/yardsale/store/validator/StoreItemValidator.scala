package com.yardsale.store.validator

import com.yardsale.store.domain.StoreItem

class StoreItemValidator extends Validator[StoreItem] {

  override def validateFields(storeItem: StoreItem): Seq[Violation] = {
    verify(storeItem.id < 0)("id", "post_store_item.id.invalid") ++
      verify(storeItem.name == null || storeItem.name.length <= 3)("name", "post_store_item.name.invalid") ++
      verify(storeItem.price < 0)("price", "post_store_item.price.invalid")
  }
}

object StoreItemValidator {
  val validator: StoreItemValidator = new StoreItemValidator()

  def apply(): StoreItemValidator = validator
}