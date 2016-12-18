package com.yardsale.store.service

import akka.actor.{Actor, UnhandledMessage}
import com.yardsale.store.dao.StoreDao
import com.yardsale.store.dao.entity.StoreItemEntity
import com.yardsale.store.domain.StoreItem
import com.yardsale.store.mapper.Mapper
import com.yardsale.store.service.StoreActor.{StoreItemPosted, _}
import com.yardsale.store.validator.Validator
import com.yardsale.store.validator.Validator.CommandViolated

class StoreActor(storeDao: StoreDao,
                 storeItemMapper: Mapper[StoreItem, StoreItemEntity],
                 postStoreItemValidator: Validator[PostStoreItem],
                 updateStoreItemValidator: Validator[UpdateStoreItem],
                 deleteStoreItemValidator: Validator[DeleteStoreItem]) extends Actor {

  override def receive: Receive = {
    case postCommand@PostStoreItem(storeItem) =>
      val violations = postStoreItemValidator.validate(postCommand)
      if (violations.isEmpty) {
        storeDao.insertStoreItem(storeItemMapper.mapModel(storeItem))
        sender() ! StoreItemPosted(storeItem)
        context.system.eventStream.publish(StoreItemPosted(storeItem))
      }
      else {
        sender() ! CommandViolated(postCommand, violations)
      }

    case updateCommand@UpdateStoreItem(storeItem) =>
      val violations = updateStoreItemValidator.validate(updateCommand)
      if (violations.isEmpty) {
        storeDao.updateStoreItem(storeItemMapper.mapModel(storeItem))
        sender() ! StoreItemUpdated(storeItem)
      }
      else {
        sender() ! CommandViolated(updateCommand, violations)
      }

    case deleteCommand@DeleteStoreItem(storeItemId) =>
      val violations = deleteStoreItemValidator.validate(deleteCommand)
      if (violations.isEmpty) {
        storeDao.deleteStoreItem(storeItemId)
        sender() ! StoreItemDeleted(storeItemId)
      }
      else {
        sender() ! CommandViolated(deleteCommand, violations)
      }
  }

}

object StoreActor {

  case class PostStoreItem(storeItem: StoreItem) extends Command

  case class StoreItemPosted(storeItem: StoreItem)

  case class UpdateStoreItem(storeItem: StoreItem) extends Command

  case class StoreItemUpdated(storeItem: StoreItem)

  case class DeleteStoreItem(storeItemId: Long) extends Command

  case class StoreItemDeleted(storeItemId: Long)

}