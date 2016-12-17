package com.yardsale.store.service

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.yardsale.store.dao.StoreDao
import com.yardsale.store.dao.entity.StoreItemEntity
import com.yardsale.store.domain.StoreItem
import com.yardsale.store.mapper.Mapper
import com.yardsale.store.service.StoreActor.{DeleteStoreItem, PostStoreItem, UpdateStoreItem}
import com.yardsale.store.validator.{Validator, Violation}
import org.scalamock.scalatest.MockFactory
import org.scalatest._

class StoreActorTest extends TestKit(ActorSystem("testSystem"))
  with ImplicitSender
  with FunSuiteLike
  with Matchers
  with OneInstancePerTest
  with MockFactory {

  var mockStoreDao = stub[StoreDao]
  var mockStoreItemMapper = stub[Mapper[StoreItem, StoreItemEntity]]
  var mockPostStoreItemCommandValidator = stub[Validator[PostStoreItem]]
  var mockUpdateStoreItemCommandValidator = stub[Validator[UpdateStoreItem]]
  var mockDeleteStoreItemCommandValidator = stub[Validator[DeleteStoreItem]]
  var actorRef = TestActorRef(new StoreActor(mockStoreDao, mockStoreItemMapper, mockPostStoreItemCommandValidator,
    mockUpdateStoreItemCommandValidator, mockDeleteStoreItemCommandValidator))

  test("Receive PostStoreItem When command with valid data Then insert Store Item") {
    val storeItem = new StoreItem
    val storeItemEntity = new StoreItemEntity
    val command = PostStoreItem(storeItem)
    (mockStoreItemMapper.mapModel _).when(storeItem).returning(storeItemEntity)
    (mockPostStoreItemCommandValidator.validate _).when(command).returning(List.empty)

    actorRef ! command

    (mockPostStoreItemCommandValidator.validate _).verify(command).once()
    (mockStoreDao.insertStoreItem _).verify(storeItemEntity).once()
  }

  test("Receive PostStoreItem When command with invalid data Then no insert") {
    val command = PostStoreItem(new StoreItem)
    (mockPostStoreItemCommandValidator.validate _).when(command).returning(List(Violation("field", "error message")))

    actorRef ! command

    (mockStoreDao.insertStoreItem _).verify(*).never()
  }

  test("Receive UpdateStoreItem When command with valid data Then update Store Item") {
    val storeItem = new StoreItem
    val storeItemEntity = new StoreItemEntity
    val command = UpdateStoreItem(storeItem)
    (mockStoreItemMapper.mapModel _).when(storeItem).returning(storeItemEntity)
    (mockUpdateStoreItemCommandValidator.validate _).when(command).returning(List.empty)

    actorRef ! command

    (mockUpdateStoreItemCommandValidator.validate _).verify(command).once()
    (mockStoreDao.updateStoreItem _).verify(storeItemEntity).once()
  }

  test("Receive UpdateStoreItem When command with invalid data Then no update") {
    val command = UpdateStoreItem(new StoreItem)
    (mockUpdateStoreItemCommandValidator.validate _).when(command).returning(List(Violation("field", "error message")))

    actorRef ! command

    (mockStoreDao.updateStoreItem _).verify(*).never()
  }

  test("Receive DeleteStoreItem When command with valid data Then delete Store Item") {
    val storeItemId: Long = 1L
    (mockDeleteStoreItemCommandValidator.validate _).when(*).returning(List.empty)
    actorRef ! DeleteStoreItem(storeItemId)

    (mockStoreDao.deleteStoreItem _).verify(storeItemId).once()
  }

  test("Receive DeleteStoreItem When command with 0 id Then no delete") {
    val command = DeleteStoreItem(-1)
    (mockDeleteStoreItemCommandValidator.validate _).when(command).returning(List(Violation("field", "error message")))

    actorRef ! command

    (mockStoreDao.deleteStoreItem _).verify(*).never()
  }

}
