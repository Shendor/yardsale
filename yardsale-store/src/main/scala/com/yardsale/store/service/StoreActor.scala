package com.yardsale.store.service

import akka.actor.Actor
import com.yardsale.store.dao.StoreDao
import com.yardsale.store.dao.entity.StoreItemEntity
import com.yardsale.store.domain.StoreItem
import com.yardsale.store.mapper.Mapper
import com.yardsale.store.service.StoreActor._
import com.yardsale.store.validator.Validator

class StoreActor(storeDao: StoreDao,
                 storeItemMapper: Mapper[StoreItem, StoreItemEntity],
                 postStoreItemValidator: Validator[PostStoreItem],
                 updateStoreItemValidator: Validator[UpdateStoreItem],
                 deleteStoreItemValidator: Validator[DeleteStoreItem]) extends Actor {

  override def receive: Receive = {
    case postCommand@PostStoreItem(storeItem) =>
      val violations = postStoreItemValidator.validate(postCommand)
      if (violations.isEmpty)
        storeDao.insertStoreItem(storeItemMapper.mapModel(storeItem))

    case updateCommand@UpdateStoreItem(storeItem) =>
      val violations = updateStoreItemValidator.validate(updateCommand)
      if (violations.isEmpty)
        storeDao.updateStoreItem(storeItemMapper.mapModel(storeItem))

    case deleteCommand@DeleteStoreItem(storeItemId) =>
      val violations = deleteStoreItemValidator.validate(deleteCommand)
      if (violations.isEmpty)
        storeDao.deleteStoreItem(storeItemId)
  }

  override def unhandled(message: Any): Unit = {
    super.unhandled(message)
    println(message)
  }
}

object StoreActor {

  case class PostStoreItem(storeItem: StoreItem)

  case class UpdateStoreItem(storeItem: StoreItem)

  case class DeleteStoreItem(storeItemId: Long)

}