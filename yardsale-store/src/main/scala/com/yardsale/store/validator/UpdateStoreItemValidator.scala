package com.yardsale.store.validator

import com.yardsale.store.domain.StoreItem
import com.yardsale.store.service.StoreActor.UpdateStoreItem

class UpdateStoreItemValidator(storeItemValidator: Validator[StoreItem]) extends Validator[UpdateStoreItem] {

  override def validateFields(updateStoreItem: UpdateStoreItem): Seq[Violation] = {
    storeItemValidator.validate(updateStoreItem.storeItem)
  }
}

object UpdateStoreItemValidator {
  val validator: UpdateStoreItemValidator = new UpdateStoreItemValidator(StoreItemValidator())

  def apply(): UpdateStoreItemValidator = validator
}