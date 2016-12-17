package com.yardsale.store.validator

import com.yardsale.store.domain.StoreItem
import com.yardsale.store.service.StoreActor.PostStoreItem

class PostStoreItemValidator(storeItemValidator: Validator[StoreItem]) extends Validator[PostStoreItem] {

  override def validateFields(postStoreItem: PostStoreItem): Seq[Violation] = {
    storeItemValidator.validate(postStoreItem.storeItem)
  }
}
