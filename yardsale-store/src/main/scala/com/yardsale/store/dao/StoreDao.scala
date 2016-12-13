package com.yardsale.store.dao

import java.time.LocalDateTime

import com.yardsale.store.dao.entity.StoreItemEntity

trait StoreDao {

  def insertStoreItem(storeItem: StoreItemEntity)

  def updateStoreItem(storeItem: StoreItemEntity)

  def deleteStoreItem(storeItemId: Long)

  def findStoreItem(storeItemId: Long): Option[StoreItemEntity]

  def findStoreItemsByUserId(userId: Long): Seq[StoreItemEntity]

  def findStoreItemsFromDate(date: LocalDateTime): Seq[StoreItemEntity]

}
