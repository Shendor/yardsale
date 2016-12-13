package com.yardsale.store.service

import java.time.LocalDateTime

import akka.actor.Actor
import com.yardsale.store.dao.StoreDao
import com.yardsale.store.service.StoreQueryActor.{GetLatestStoreItems, GetStoreItem, GetStoreItemsOfUser}

class StoreQueryActor(val storeDao: StoreDao) extends Actor {

  override def receive: Receive = {
    case GetStoreItem(storeItemId) => storeDao.findStoreItem(storeItemId)
    case GetStoreItemsOfUser(userId) => storeDao.findStoreItemsByUserId(userId)
    case GetLatestStoreItems() => storeDao.findStoreItemsFromDate(LocalDateTime.now())
  }
}

object StoreQueryActor {

  case class GetStoreItem(storeItemId: Long)

  case class GetStoreItemsOfUser(userId: Long)

  case class GetLatestStoreItems()

}
