package com.yardsale.store.validator

import com.yardsale.store.service.StoreActor.DeleteStoreItem

class DeleteStoreItemValidator() extends Validator[DeleteStoreItem] {

  override def validateFields(deleteStoreItem: DeleteStoreItem): Seq[Violation] = {
    verify(deleteStoreItem.storeItemId < 1)("id", "post_store_item.id.invalid")
  }
}
