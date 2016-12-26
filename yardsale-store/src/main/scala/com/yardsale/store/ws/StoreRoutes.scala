package com.yardsale.store.ws

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._

import scala.concurrent.ExecutionContext

class StoreRoutes(actorSystem: ActorSystem)(implicit executionContext: ExecutionContext) {

  val storeRoute = new StoreRoute(actorSystem)
  val storeQueryRoute = new StoreQueryRoute(actorSystem)

  val routes =
    pathPrefix("store") {
      storeRoute.route ~
        storeQueryRoute.route
    }
}

object StoreRoutes {
  def apply(actorSystem: ActorSystem)(implicit executionContext: ExecutionContext): StoreRoutes = new StoreRoutes(actorSystem)
}