package com.yardsale.store.ws

import akka.actor.{Actor, ActorSystem, Props}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.{RouteTest, ScalatestRouteTest}
import akka.testkit.TestActorRef
import com.yardsale.store.domain.StoreItem
import com.yardsale.store.service.QueryResult
import com.yardsale.store.service.StoreQueryActor.{GetLatestStoreItems, GetStoreItem, GetStoreItemsOfUser}
import com.yardsale.store.util.JacksonJsonSupport
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, Matchers}

import scala.util.Failure

class StoreQueryRouteTest extends FunSuite
  with Matchers
  with ScalatestRouteTest
  with RouteTest
  with MockFactory
  with JacksonJsonSupport {

  val mockActorSystem = stub[ActorSystem]

  test("Test get latest store items When one item exist Then return this item") {
    val actorRef = TestActorRef(new Actor {
      def receive = {
        case GetLatestStoreItems() =>
          sender() forward QueryResult(List(new StoreItem()))
      }
    })
    (mockActorSystem.actorOf(_: Props, _: String)).when(*, *).returning(actorRef)

    val storeQueryRoute: StoreQueryRoute = new StoreQueryRoute(mockActorSystem)

    Get("/latest") ~> storeQueryRoute.route ~> check {
      val result = responseAs[JsonResponse[List[Any]]]

      result.response.size should be(1)
      response.status should be(StatusCodes.OK)
    }
  }

  test("Test get latest store items When internal server error Then return this error") {
    val actorRef = TestActorRef(new Actor {
      def receive = {
        case GetLatestStoreItems() => sender() forward Failure(new RuntimeException("oops"))
      }
    })
    (mockActorSystem.actorOf(_: Props, _: String)).when(*, *).returning(actorRef)

    val storeQueryRoute: StoreQueryRoute = new StoreQueryRoute(mockActorSystem)

    Get("/latest") ~> storeQueryRoute.route ~> check {
      response.status should be(StatusCodes.InternalServerError)
    }
  }

  test("Test get store item by id When one item exist Then return this item") {
    val idToFind: Long = 100
    val actorRef = TestActorRef(new Actor {
      def receive = {
        case GetStoreItem(id) =>
          sender() forward QueryResult(new StoreItem() {
            id = idToFind
          })
      }
    })
    (mockActorSystem.actorOf(_: Props, _: String)).when(*, *).returning(actorRef)

    val storeQueryRoute: StoreQueryRoute = new StoreQueryRoute(mockActorSystem)

    Get("/get/" + idToFind) ~> storeQueryRoute.route ~> check {
      val result = responseAs[JsonResponse[Map[String, Any]]]

      result.response.get("id").get should be(idToFind)
      response.status should be(StatusCodes.OK)
    }
  }

  test("Test get store item by user When one item exist Then return this item") {
    val userIdToFind: Long = 100
    val actorRef = TestActorRef(new Actor {
      def receive = {
        case GetStoreItemsOfUser(userId) =>
          sender() forward QueryResult(new StoreItem() {
            userId = userIdToFind
          })
      }
    })
    (mockActorSystem.actorOf(_: Props, _: String)).when(*, *).returning(actorRef)

    val storeQueryRoute: StoreQueryRoute = new StoreQueryRoute(mockActorSystem)

    Get("/user/" + userIdToFind + "/items") ~> storeQueryRoute.route ~> check {
      val result = responseAs[JsonResponse[Map[String, Any]]]

      result.response.get("userId").get should be(userIdToFind)
      response.status should be(StatusCodes.OK)
    }
  }
}
