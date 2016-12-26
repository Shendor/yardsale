package com.yardsale.store.dao

import java.time.LocalDateTime

import com.yardsale.store.dao.entity.StoreItemEntity


class InMemoryStoreDao extends StoreDao {

  var storeItems = Map[Long, StoreItemEntity]()
  var count = 0

  override def insertStoreItem(storeItem: StoreItemEntity): Unit = {
    storeItems += (storeItem.id -> storeItem)
    count += 1
  }

  override def findStoreItemsFromDate(date: LocalDateTime): List[StoreItemEntity] = {
    (storeItems filter (storeItem => storeItem._2.postedDate.isBefore(date))).values toList
  }

  override def updateStoreItem(storeItem: StoreItemEntity): Unit = {
    storeItems get storeItem.id match {
      case Some(entity) => {
        entity.name = storeItem.name
        entity.description = storeItem.description
        entity.price = storeItem.price
        entity.category = storeItem.category
        entity.imagesUrl = storeItem.imagesUrl
        entity.filter = storeItem.filter
        entity.postedDate = storeItem.postedDate
      }
      case None => println(s"Store item ${storeItem} not found")
    }
  }

  override def findStoreItemsByUserId(userId: Long): Iterable[StoreItemEntity] = {
    (storeItems filter (storeItem => storeItem._2.userId == userId)).values toList
  }

  override def deleteStoreItem(storeItemId: Long): Unit = {
    storeItems -= storeItemId
  }

  override def findStoreItem(storeItemId: Long): Option[StoreItemEntity] = {
    storeItems get storeItemId
  }
}

object InMemoryStoreDao {
  val dao = new InMemoryStoreDao()

  def apply() = dao
}

