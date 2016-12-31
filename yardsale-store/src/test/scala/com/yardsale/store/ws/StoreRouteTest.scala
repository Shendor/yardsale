package com.yardsale.store.ws

import akka.actor.{Actor, ActorSystem, Props, Status}
import akka.http.scaladsl.model.{HttpEntity, MediaTypes, StatusCodes}
import akka.http.scaladsl.testkit.{RouteTest, ScalatestRouteTest}
import akka.stream.scaladsl.Sink
import akka.testkit.TestActorRef
import com.yardsale.store.domain.StoreItem
import com.yardsale.store.service.StoreActor._
import com.yardsale.store.util.{JacksonJsonSupport, JsonUtil}
import com.yardsale.store.validator.Validator.CommandViolated
import com.yardsale.store.validator.Violation
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

class StoreRouteTest extends FunSuite
  with Matchers
  with ScalatestRouteTest
  with RouteTest
  with MockFactory
  with JacksonJsonSupport {

  val mockActorSystem = stub[ActorSystem]

  test("Test post store item When valid request Then return success") {
    val actorRef = TestActorRef(new Actor {
      def receive = {
        case PostStoreItem(storeItem) => sender() forward StoreItemPosted(storeItem)
      }
    })
    (mockActorSystem.actorOf(_: Props, _: String)).when(*, *).returning(actorRef)
    val storeRoute = new StoreRoute(mockActorSystem)

    val storeItemToPost = new StoreItem()
    storeItemToPost.id = 100
    val requestEntity = HttpEntity(MediaTypes.`application/json`, JsonUtil.toJson(storeItemToPost))

    Put("/post/item", requestEntity) ~> storeRoute.route ~> check {
      val jsonResponse = responseAs[JsonResponse[Map[String, Any]]]

      jsonResponse.response.getOrElse("id", 0) should be(storeItemToPost.id)
      response.status should be(StatusCodes.OK)
    }
  }

  test("Test post store item When request has one violation Then return CommandViolated event with this violation") {
    val actorRef = TestActorRef(new Actor {
      def receive = {
        case command@PostStoreItem(storeItem) => sender() forward CommandViolated(command, List(Violation("f1", "error")))
      }
    })
    (mockActorSystem.actorOf(_: Props, _: String)).when(*, *).returning(actorRef)
    val storeRoute = new StoreRoute(mockActorSystem)

    val requestEntity = HttpEntity(MediaTypes.`application/json`, JsonUtil.toJson(new StoreItem()))

    Put("/post/item", requestEntity) ~> storeRoute.route ~> check {
      val result = responseAs[JsonResponse[Any]]

      assert(result.violations.size == 1)
      response.status should be(StatusCodes.OK)
    }
  }

  test("Test post store item When request failed Then return error") {
    val errorMessage: String = "oops"
    val actorRef = TestActorRef(new Actor {
      def receive = {
        case command@PostStoreItem(storeItem) =>
          sender() ! Status.Failure(new RuntimeException(errorMessage))
      }
    })
    (mockActorSystem.actorOf(_: Props, _: String)).when(*, *).returning(actorRef)
    val storeRoute = new StoreRoute(mockActorSystem)

    val requestEntity = HttpEntity(MediaTypes.`application/json`, JsonUtil.toJson(new StoreItem()))

    Put("/post/item", requestEntity) ~> storeRoute.route ~> check {
      val responseContent = response.entity.dataBytes.map(_.utf8String).runWith(Sink.lastOption)

      val content: Option[String] = Await.result(responseContent, 10.seconds)
      content.get should be(errorMessage)
      response.status should be(StatusCodes.InternalServerError)
    }
  }

  test("Test update store item When valid request Then return success") {
    val actorRef = TestActorRef(new Actor {
      def receive = {
        case UpdateStoreItem(storeItem) => sender() forward StoreItemUpdated(storeItem)
      }
    })
    (mockActorSystem.actorOf(_: Props, _: String)).when(*, *).returning(actorRef)
    val storeRoute = new StoreRoute(mockActorSystem)

    val storeItemToUpdate = new StoreItem()
    storeItemToUpdate.id = 100
    val requestEntity = HttpEntity(MediaTypes.`application/json`, JsonUtil.toJson(storeItemToUpdate))

    Post("/update/item", requestEntity) ~> storeRoute.route ~> check {
      val result = responseAs[JsonResponse[Map[String, Any]]]

      result.response.getOrElse("id", 0) should be(storeItemToUpdate.id)
      response.status should be(StatusCodes.OK)
    }
  }

  test("Test delete store item When valid request Then return success") {
    val actorRef = TestActorRef(new Actor {
      def receive = {
        case DeleteStoreItem(storeItemId) => sender() forward StoreItemDeleted(storeItemId)
      }
    })
    (mockActorSystem.actorOf(_: Props, _: String)).when(*, *).returning(actorRef)
    val storeRoute = new StoreRoute(mockActorSystem)

    val id = 100
    Delete("/delete/item/" + id) ~> storeRoute.route ~> check {
      val result = responseAs[JsonResponse[Long]]

      result.response should be(id)
      response.status should be(StatusCodes.OK)
    }
  }
}
