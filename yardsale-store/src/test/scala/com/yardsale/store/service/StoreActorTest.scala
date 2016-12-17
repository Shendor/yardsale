package com.yardsale.store.service

import akka.actor.{ActorSystem, UnhandledMessage}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.yardsale.store.dao.StoreDao
import com.yardsale.store.dao.entity.StoreItemEntity
import com.yardsale.store.domain.StoreItem
import com.yardsale.store.mapper.Mapper
import com.yardsale.store.service.StoreActor._
import com.yardsale.store.validator.Validator.CommandViolated
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

  test("Receive PostStoreItem When command with valid data Then insert Store Item and send message") {
    val storeItem = new StoreItem
    val storeItemEntity = new StoreItemEntity
    val command = PostStoreItem(storeItem)
    (mockStoreItemMapper.mapModel _).when(storeItem).returning(storeItemEntity)
    (mockPostStoreItemCommandValidator.validate _).when(command).returning(List.empty)

    actorRef ! command

    (mockPostStoreItemCommandValidator.validate _).verify(command).once()
    (mockStoreDao.insertStoreItem _).verify(storeItemEntity).once()
    expectMsg(StoreItemPosted(storeItem))
  }

  test("Receive PostStoreItem When command with invalid data Then no insert and send message with violations") {
    val command = PostStoreItem(new StoreItem)
    val violations = List(Violation("field", "error message"))
    (mockPostStoreItemCommandValidator.validate _).when(command).returning(violations)

    actorRef ! command

    (mockStoreDao.insertStoreItem _).verify(*).never()
    expectMsg(CommandViolated(command, violations))
  }

  test("Receive UpdateStoreItem When command with valid data Then update Store Item and send message") {
    val storeItem = new StoreItem
    val storeItemEntity = new StoreItemEntity
    val command = UpdateStoreItem(storeItem)
    (mockStoreItemMapper.mapModel _).when(storeItem).returning(storeItemEntity)
    (mockUpdateStoreItemCommandValidator.validate _).when(command).returning(List.empty)

    actorRef ! command

    (mockUpdateStoreItemCommandValidator.validate _).verify(command).once()
    (mockStoreDao.updateStoreItem _).verify(storeItemEntity).once()
    expectMsg(StoreItemUpdated(storeItem))
  }

  test("Receive UpdateStoreItem When command with invalid data Then no update and send message with violations") {
    val command = UpdateStoreItem(new StoreItem)
    val violations = List(Violation("field", "error message"))
    (mockUpdateStoreItemCommandValidator.validate _).when(command).returning(violations)

    actorRef ! command

    (mockStoreDao.updateStoreItem _).verify(*).never()
    expectMsg(CommandViolated(command, violations))
  }

  test("Receive DeleteStoreItem When command with valid data Then delete Store Item and send message") {
    val storeItemId: Long = 1L
    (mockDeleteStoreItemCommandValidator.validate _).when(*).returning(List.empty)
    actorRef ! DeleteStoreItem(storeItemId)

    (mockStoreDao.deleteStoreItem _).verify(storeItemId).once()
    expectMsg(StoreItemDeleted(storeItemId))
  }

  test("Receive DeleteStoreItem When command with 0 id Then no delete and send message with violations") {
    val command = DeleteStoreItem(-1)
    val violations = List(Violation("field", "error message"))
    (mockDeleteStoreItemCommandValidator.validate _).when(command).returning(violations)

    actorRef ! command

    (mockStoreDao.deleteStoreItem _).verify(*).never()
    expectMsg(CommandViolated(command, violations))
  }

}
