package com.yardsale.store.mapper

import java.time.LocalDateTime

import com.yardsale.store.dao.entity.StoreItemEntity
import com.yardsale.store.domain.StoreItem
import org.scalatest.{FreeSpec, Matchers}

class StoreItemMapperTest extends FreeSpec {

  "Given Store Item Mapper" - {
    val mapper = new StoreItemMapper

    "When maps entity with all set up fields" - {
      val storeItemEntity: StoreItemEntity = new StoreItemEntity()
      storeItemEntity.id = 100
      storeItemEntity.name = "name"
      storeItemEntity.description = "description"
      storeItemEntity.category = "cat1;sub-cat1"
      storeItemEntity.filter = "filter1;filter2"
      storeItemEntity.postedDate = LocalDateTime.now()
      storeItemEntity.imagesUrl = "url1;url2"
      storeItemEntity.price = 999
      storeItemEntity.userId = 1
      val storeItem: StoreItem = mapper.mapEntity(storeItemEntity)

      "Then mapped with correct data" in {
        Matchers.assert(storeItemEntity.id.equals(storeItem.id))
        Matchers.assert(storeItemEntity.name.equals(storeItem.name))
        Matchers.assert(storeItemEntity.description.equals(storeItem.description))
        Matchers.assert(storeItemEntity.postedDate.equals(storeItem.postedDate))
        Matchers.assert(storeItemEntity.price.equals(storeItem.price))
        Matchers.assert(storeItemEntity.userId.equals(storeItem.userId))
        Matchers.assert(storeItem.categories.size.equals(2))
        Matchers.assert(storeItem.filters.size.equals(2))
        Matchers.assert(storeItem.imagesUrl.size.equals(2))

        storeItem.categories.foreach(item => Matchers.assert(storeItemEntity.category.contains(item)))
        storeItem.filters.foreach(item => Matchers.assert(storeItemEntity.filter.contains(item)))
        storeItem.imagesUrl.foreach(item => Matchers.assert(storeItemEntity.imagesUrl.contains(item)))
      }
    }

    "When maps entity without filter, category and images" - {
      val storeItemEntity: StoreItemEntity = new StoreItemEntity()
      val storeItem: StoreItem = mapper.mapEntity(storeItemEntity)

      "Then mapped with empty lists" in {
        Matchers.assert(storeItem.categories.isEmpty)
        Matchers.assert(storeItem.filters.isEmpty)
        Matchers.assert(storeItem.imagesUrl.isEmpty)
      }
    }

    "When maps Store Item with all set up fields" - {
      val storeItem: StoreItem = new StoreItem
      storeItem.id = 100
      storeItem.name = "name"
      storeItem.description = "description"
      storeItem.categories = List("cat1", "sub-cat1", "sub-sub-cat1")
      storeItem.filters = List("filter1", "filter2", "filter3")
      storeItem.postedDate = LocalDateTime.now()
      storeItem.imagesUrl = List("url1", "url2", "url3", "url4")
      storeItem.price = 999
      storeItem.userId = 1
      val storeItemEntity: StoreItemEntity = mapper.mapModel(storeItem)

      "Then mapped with correct data" in {
        Matchers.assert(storeItem.id.equals(storeItemEntity.id))
        Matchers.assert(storeItem.name.equals(storeItemEntity.name))
        Matchers.assert(storeItem.description.equals(storeItemEntity.description))
        Matchers.assert(storeItem.postedDate.equals(storeItemEntity.postedDate))
        Matchers.assert(storeItem.price.equals(storeItemEntity.price))
        Matchers.assert(storeItem.userId.equals(storeItemEntity.userId))

        storeItem.categories.foreach(item => Matchers.assert(storeItemEntity.category.contains(item)))
        storeItem.filters.foreach(item => Matchers.assert(storeItemEntity.filter.contains(item)))
        storeItem.imagesUrl.foreach(item => Matchers.assert(storeItemEntity.imagesUrl.contains(item)))
      }
    }

    "When maps Store Item without filter, category and images" - {
      val storeItem: StoreItem = new StoreItem
      val storeItemEntity: StoreItemEntity = mapper.mapModel(storeItem)

      "Then mapped with empty String" in {
        Matchers.assert(storeItemEntity.category.isEmpty)
        Matchers.assert(storeItemEntity.filter.isEmpty)
        Matchers.assert(storeItemEntity.imagesUrl.isEmpty)
      }
    }
  }

}
