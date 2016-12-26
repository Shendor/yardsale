package com.yardsale.store.mapper

import java.time.LocalDateTime

import com.yardsale.store.dao.entity.StoreItemEntity
import com.yardsale.store.domain.StoreItem

class StoreItemMapper extends Mapper[StoreItem, StoreItemEntity] {

  val splitter: String = ";"

  override def mapEntity(entity: StoreItemEntity): StoreItem = {
    val storeItem: StoreItem = new StoreItem

    storeItem.id = entity.id
    storeItem.name = entity.name
    storeItem.description = entity.description
    storeItem.price = entity.price
    storeItem.postedDate = entity.postedDate
    storeItem.userId = entity.userId
    storeItem.imagesUrl = if (Option(entity.imagesUrl).isEmpty) List.empty else entity.imagesUrl.split(splitter).toList
    storeItem.categories = if (Option(entity.category).isEmpty) List.empty else entity.category.split(splitter).toList
    storeItem.filters = if (Option(entity.filter).isEmpty) List.empty else entity.filter.split(splitter).toList

    storeItem
  }

  override def mapModel(storeItem: StoreItem): StoreItemEntity = {
    val concatenation: (String, String) => String =
      (previousItem, item) => previousItem match {
        case "" => item.toString
        case _ => previousItem ++ splitter ++ item.toString
      }

    if(storeItem.postedDate == null) storeItem.postedDate = LocalDateTime.now()

    val storeItemEntity: StoreItemEntity = new StoreItemEntity
    storeItemEntity.id = storeItem.id
    storeItemEntity.name = storeItem.name
    storeItemEntity.description = storeItem.description
    storeItemEntity.price = storeItem.price
    storeItemEntity.postedDate = storeItem.postedDate
    storeItemEntity.userId = storeItem.userId
    storeItemEntity.imagesUrl = storeItem.imagesUrl.foldLeft("")(concatenation)
    storeItemEntity.category = storeItem.categories.foldLeft("")(concatenation)
    storeItemEntity.filter = storeItem.filters.foldLeft("")(concatenation)

    storeItemEntity
  }
}

object StoreItemMapper {
  val mapper = new StoreItemMapper()

  def apply(): StoreItemMapper = mapper
}
