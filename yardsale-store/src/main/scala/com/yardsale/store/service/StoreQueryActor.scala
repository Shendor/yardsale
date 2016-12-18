package com.yardsale.store.service

import java.time.LocalDateTime

import akka.actor.Actor
import com.yardsale.store.dao.StoreDao
import com.yardsale.store.dao.entity.StoreItemEntity
import com.yardsale.store.domain.StoreItem
import com.yardsale.store.mapper.Mapper
import com.yardsale.store.service.StoreActor.{StoreItemDeleted, StoreItemPosted, StoreItemUpdated}
import com.yardsale.store.service.StoreQueryActor.{GetLatestStoreItems, GetStoreItem, GetStoreItemsOfUser}

class StoreQueryActor(storeDao: StoreDao,
                      storeItemMapper: Mapper[StoreItem, StoreItemEntity]) extends Actor {

  var latestStoreItems: List[StoreItem] = List.empty

  override def receive: Receive = {

    case command@GetStoreItem(storeItemId) => {
      storeDao.findStoreItem(storeItemId) match {
        case Some(entity) => sender() ! QueryResult[StoreItem](storeItemMapper.mapEntity(entity))
        case None => sender() ! QueryResult(None)
      }
    }

    case command@GetStoreItemsOfUser(userId) => {
      val storeItems = storeDao.findStoreItemsByUserId(userId) map storeItemMapper.mapEntity
      sender() ! QueryResult[Seq[StoreItem]](storeItems)
    }

    case command@GetLatestStoreItems() => {
      sender() ! QueryResult[Seq[StoreItem]](latestStoreItems)
    }

    case StoreItemPosted(storeItem) => {
      latestStoreItems = storeItem :: (latestStoreItems take 19)
    }

    case StoreItemDeleted(storeItemId) => {
      initializeLatestStoreItemsIfMatchId(storeItemId)
    }

    case StoreItemUpdated(storeItem) => {
      initializeLatestStoreItemsIfMatchId(storeItem.id)
    }
  }

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    super.preStart()
    initializeLatestStoreItems()
    context.system.eventStream.subscribe(context.self, classOf[StoreItemPosted])
    context.system.eventStream.subscribe(context.self, classOf[StoreItemDeleted])
    context.system.eventStream.subscribe(context.self, classOf[StoreItemUpdated])
  }

  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    super.postStop()
    context.system.eventStream.unsubscribe(context.self)
  }

  private def initializeLatestStoreItemsIfMatchId(storeItemId: Long): Unit = {
    if (latestStoreItems.count(i => i.id == storeItemId) == 1) {
      initializeLatestStoreItems()
    }
  }

  private def initializeLatestStoreItems(): Unit = {
    latestStoreItems = storeDao.findStoreItemsFromDate(LocalDateTime.now()) map storeItemMapper.mapEntity
  }
}

object StoreQueryActor {

  case class GetStoreItem(storeItemId: Long) extends Command

  case class GetStoreItemsOfUser(userId: Long) extends Command

  case class GetLatestStoreItems() extends Command

}
