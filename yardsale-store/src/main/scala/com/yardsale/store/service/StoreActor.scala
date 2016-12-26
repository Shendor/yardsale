package com.yardsale.store.service

import akka.actor.Actor
import akka.event.Logging
import com.yardsale.store.dao.StoreDao
import com.yardsale.store.dao.entity.StoreItemEntity
import com.yardsale.store.domain.StoreItem
import com.yardsale.store.mapper.{Mapper, StoreItemMapper}
import com.yardsale.store.service.StoreActor.{StoreItemPosted, _}
import com.yardsale.store.validator.Validator.CommandViolated
import com.yardsale.store.validator.{DeleteStoreItemValidator, PostStoreItemValidator, UpdateStoreItemValidator, Validator}

class StoreActor(storeDao: StoreDao,
                 storeItemMapper: Mapper[StoreItem, StoreItemEntity],
                 postStoreItemValidator: Validator[PostStoreItem],
                 updateStoreItemValidator: Validator[UpdateStoreItem],
                 deleteStoreItemValidator: Validator[DeleteStoreItem]) extends Actor {

  val log = Logging(context.system, this)

  override def receive: Receive = {
    case postCommand@PostStoreItem(storeItem) =>
      log.info("PostStoreItem has been received")
      val violations = postStoreItemValidator.validate(postCommand)
      if (violations.isEmpty) {
        storeDao.insertStoreItem(storeItemMapper.mapModel(storeItem))
        log.info("Store Item is posted successfully")

        sender() ! StoreItemPosted(storeItem)
        context.system.eventStream.publish(StoreItemPosted(storeItem))
      }
      else {
        log.error("PostStoreItem is invalid")
        sender() ! CommandViolated(postCommand, violations)
      }

    case updateCommand@UpdateStoreItem(storeItem) =>
      log.info("UpdateStoreItem has been received")
      val violations = updateStoreItemValidator.validate(updateCommand)

      if (violations.isEmpty) {
        storeDao.updateStoreItem(storeItemMapper.mapModel(storeItem))
        log.info("Store Item is updated successfully")

        sender() ! StoreItemUpdated(storeItem)
        context.system.eventStream.publish(StoreItemUpdated(storeItem))
      }
      else {
        log.error("UpdateStoreItem is invalid")
        sender() ! CommandViolated(updateCommand, violations)
      }

    case deleteCommand@DeleteStoreItem(storeItemId) =>
      log.info("UpdateStoreItem has been received")
      val violations = deleteStoreItemValidator.validate(deleteCommand)

      if (violations.isEmpty) {
        storeDao.deleteStoreItem(storeItemId)
        log.info("Store Item is deleted successfully")

        sender() ! StoreItemDeleted(storeItemId)
        context.system.eventStream.publish(StoreItemDeleted(storeItemId))
      }
      else {
        log.error("DeleteStoreItem is invalid")
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

  def apply(storeDao: StoreDao): StoreActor =
    new StoreActor(storeDao, StoreItemMapper(), PostStoreItemValidator(), UpdateStoreItemValidator(), DeleteStoreItemValidator())

}