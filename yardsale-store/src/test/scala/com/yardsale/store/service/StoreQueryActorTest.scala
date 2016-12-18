package com.yardsale.store.service

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.yardsale.store.dao.StoreDao
import com.yardsale.store.dao.entity.StoreItemEntity
import com.yardsale.store.domain.StoreItem
import com.yardsale.store.mapper.Mapper
import com.yardsale.store.service.StoreActor.{StoreItemDeleted, StoreItemPosted, StoreItemUpdated}
import com.yardsale.store.service.StoreQueryActor.{GetLatestStoreItems, GetStoreItem, GetStoreItemsOfUser}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuiteLike, Matchers, OneInstancePerTest}

class StoreQueryActorTest extends TestKit(ActorSystem("testSystem"))
  with ImplicitSender
  with FunSuiteLike
  with Matchers
  with OneInstancePerTest
  with MockFactory {

  var mockStoreDao = stub[StoreDao]
  (mockStoreDao.findStoreItemsFromDate(_)).when(*).returning(List.empty)
  var mockStoreItemMapper = stub[Mapper[StoreItem, StoreItemEntity]]
  var actorRef = TestActorRef(new StoreQueryActor(mockStoreDao, mockStoreItemMapper))

  test("Test get store item When id is valid Then return found store item") {
    val storeItemId = 1L
    val storeItemEntity = new StoreItemEntity
    val storeItem = new StoreItem
    val command = GetStoreItem(storeItemId)
    (mockStoreItemMapper.mapEntity(_)).when(storeItemEntity).returning(storeItem)
    (mockStoreDao.findStoreItem(_)).when(storeItemId).returning(Some(storeItemEntity))

    actorRef ! command

    expectMsg(QueryResult[StoreItem](storeItem))
  }

  test("Test get store item When no items in storage Then return nothing") {
    val storeItemId = 1L
    val command = GetStoreItem(storeItemId)
    (mockStoreDao.findStoreItem(_)).when(storeItemId).returning(None)

    actorRef ! command

    expectMsg(QueryResult(None))
  }

  test("Test get store items for user When user id is valid Then return found store items") {
    val userId = 1L
    val storeItemEntities = List(new StoreItemEntity)
    val storeItem = new StoreItem
    val command = GetStoreItemsOfUser(userId)
    (mockStoreItemMapper.mapEntity(_)).when(storeItemEntities.head).returning(storeItem)
    (mockStoreDao.findStoreItemsByUserId(_)).when(userId).returning(storeItemEntities)

    actorRef ! command

    expectMsg(QueryResult[Seq[StoreItem]](List(storeItem)))
  }

  test("Test get store items When no items in storage Then return empty list") {
    val userId = 1L
    val command = GetStoreItemsOfUser(userId)
    (mockStoreDao.findStoreItemsByUserId(_)).when(userId).returning(List.empty)

    actorRef ! command

    expectMsg(QueryResult[Seq[StoreItem]](List.empty))
  }

  test("Test get latest store items When command sent And one store item is in memory Then return found store items from cache") {
    val storeItem = new StoreItem
    system.eventStream.publish(StoreItemPosted(storeItem))

    actorRef ! GetLatestStoreItems()

    expectMsg(QueryResult[Seq[StoreItem]](List(storeItem)))
  }

  test("Test get latest store items When command sent And more than 21 items were posted Then return first 20 items") {
    var expectedItems: List[StoreItem] = List.empty
    for (i <- 1 to 21) {
      val storeItem = new StoreItem
      expectedItems = storeItem :: expectedItems
      system.eventStream.publish(StoreItemPosted(storeItem))
    }

    actorRef ! GetLatestStoreItems()

    expectMsg(QueryResult[Seq[StoreItem]](expectedItems take 20))
  }

  test("Test get latest store items When delete event sent And one of latest items was removed Then verify re-initialization") {
    val storeItem = new StoreItem
    system.eventStream.publish(StoreItemPosted(storeItem))

    system.eventStream.publish(StoreItemDeleted(storeItem.id))

    (mockStoreDao.findStoreItemsFromDate(_)).verify(*).twice() // first time is invoked from preStart
  }

  test("Test get latest store items When update event sent And one of latest items was updated Then verify re-initialization") {
    val storeItem = new StoreItem
    system.eventStream.publish(StoreItemPosted(storeItem))

    system.eventStream.publish(StoreItemUpdated(storeItem))

    (mockStoreDao.findStoreItemsFromDate(_)).verify(*).twice() // first time is invoked from preStart
  }

  test("Test get latest store items When no items in storage Then return empty list") {
    actorRef ! GetLatestStoreItems()

    expectMsg(QueryResult[Seq[StoreItem]](List.empty))
  }

}
