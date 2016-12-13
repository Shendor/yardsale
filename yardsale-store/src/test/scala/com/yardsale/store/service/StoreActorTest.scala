package com.yardsale.store.service

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.yardsale.store.dao.StoreDao
import com.yardsale.store.dao.entity.StoreItemEntity
import com.yardsale.store.service.StoreActor.{DeleteStoreItem, PostStoreItem, UpdateStoreItem}
import org.mockito.Mockito
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpecLike}

class StoreActorTest extends TestKit(ActorSystem("testSystem"))
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with MockitoSugar {

  "Store Actor" when {
    val mockStoreDao: StoreDao = mock[StoreDao]
    val actorRef = TestActorRef(new StoreActor(mockStoreDao))

    "receives PostStoreItem command" should {
      val storeItem: StoreItemEntity = new StoreItemEntity
      actorRef ! PostStoreItem(storeItem)

      "inserts Store Item Entity" in {
        Mockito.verify(mockStoreDao).insertStoreItem(storeItem)
      }
    }

    "receives PostStoreItem command with invalid" should {
      val storeItem: StoreItemEntity = new StoreItemEntity
      actorRef ! PostStoreItem(storeItem)

      "inserts Store Item Entity" in {
        Mockito.verify(mockStoreDao).insertStoreItem(storeItem)
      }
    }

    "receives UpdateStoreItem command" should {
      val storeItem: StoreItemEntity = new StoreItemEntity
      actorRef ! UpdateStoreItem(storeItem)

      "updates Store Item Entity" in {
        Mockito.verify(mockStoreDao).updateStoreItem(storeItem)
      }
    }

    "receives DeleteStoreItem command" should {
      actorRef ! DeleteStoreItem(1)

      "deletes Store Item Entity" in {
        Mockito.verify(mockStoreDao).deleteStoreItem(1)
      }
    }
  }

}
