package com.yardsale.store.service

import akka.actor.Actor
import com.yardsale.store.dao.StoreDao
import com.yardsale.store.dao.entity.StoreItemEntity
import com.yardsale.store.service.StoreActor._

class StoreActor(storeDao: StoreDao) extends Actor {

  override def receive: Receive = {
    case PostStoreItem(storeItem) => storeDao.insertStoreItem(storeItem)
    case UpdateStoreItem(storeItem) => storeDao.updateStoreItem(storeItem)
    case DeleteStoreItem(storeItemId) => storeDao.deleteStoreItem(storeItemId)
  }
}

object StoreActor {

  case class PostStoreItem(storeItem: StoreItemEntity)

  case class UpdateStoreItem(storeItem: StoreItemEntity)

  case class DeleteStoreItem(storeItemId: Long)
}