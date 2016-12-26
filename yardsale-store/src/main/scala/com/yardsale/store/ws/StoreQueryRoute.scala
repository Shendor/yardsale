package com.yardsale.store.ws

import java.util.Optional

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers.IntNumber
import akka.pattern.ask
import com.yardsale.store.dao.InMemoryStoreDao
import com.yardsale.store.domain.StoreItem
import com.yardsale.store.service.StoreQueryActor.{GetLatestStoreItems, GetStoreItem, GetStoreItemsOfUser}
import com.yardsale.store.service.{QueryResult, StoreQueryActor}

import scala.concurrent.{ExecutionContext, Future}

class StoreQueryRoute(actorSystem: ActorSystem)(implicit executionContext: ExecutionContext) extends ResponseHandlerRoute {

  private val storeQueryActor = actorSystem.actorOf(Props(StoreQueryActor(InMemoryStoreDao())), "storeQueryActor")

  val route =
    path("latest") {
      get {
        handleResponse(getLatestItems)
      }
    } ~
      path("get" / IntNumber) { id =>
        get {
          handleResponse(getStoreItem(id))
        }
      } ~
      path("user" / IntNumber / "items") { userId =>
        get {
          handleResponse(getStoreItemsOfUser(userId))
        }
      }

  def getLatestItems: Future[QueryResult[Iterable[StoreItem]]] = {
    (storeQueryActor ? GetLatestStoreItems()).mapTo[QueryResult[Iterable[StoreItem]]]
  }

  def getStoreItem(storeItemId: Long): Future[QueryResult[Optional[StoreItem]]] = {
    (storeQueryActor ? GetStoreItem(storeItemId)).mapTo[QueryResult[Optional[StoreItem]]]
  }

  def getStoreItemsOfUser(userId: Long): Future[QueryResult[Iterable[StoreItem]]] = {
    (storeQueryActor ? GetStoreItemsOfUser(userId)).mapTo[QueryResult[Iterable[StoreItem]]]
  }
}
