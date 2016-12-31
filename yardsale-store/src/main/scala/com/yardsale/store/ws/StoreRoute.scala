package com.yardsale.store.ws

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import com.yardsale.store.dao.InMemoryStoreDao
import com.yardsale.store.domain.StoreItem
import com.yardsale.store.service.StoreActor
import com.yardsale.store.service.StoreActor._
import com.yardsale.store.util.JacksonJsonSupport

import scala.concurrent.{ExecutionContext, Future}

class StoreRoute(actorSystem: ActorSystem)(implicit executionContext: ExecutionContext) extends ResponseHandlerRoute
  with JacksonJsonSupport {

  private val storeActor = actorSystem.actorOf(Props(StoreActor(InMemoryStoreDao())), "storeActor")

  val route =
    path("post" / "item") {
      pathEndOrSingleSlash {
        put {
          entity(as[StoreItem]) { storeItem =>
            handleResponse(postStoreItem(storeItem))
          }
        }
      }
    } ~
      path("update" / "item") {
        pathEndOrSingleSlash {
          post {
            entity(as[StoreItem]) { storeItem =>
              handleResponse(updateStoreItem(storeItem))
            }
          }
        }
      } ~
      path("delete" / "item" / IntNumber) { storeItemId =>
        delete {
          handleResponse(deleteStoreItem(storeItemId))
        }
      }

  def postStoreItem(storeItem: StoreItem): Future[Any] = {
    storeActor ? PostStoreItem(storeItem)
  }

  def updateStoreItem(storeItem: StoreItem): Future[Any] = {
    storeActor ? UpdateStoreItem(storeItem)
  }

  def deleteStoreItem(storeItemId: Long): Future[Any] = {
    storeActor ? DeleteStoreItem(storeItemId)
  }
}
