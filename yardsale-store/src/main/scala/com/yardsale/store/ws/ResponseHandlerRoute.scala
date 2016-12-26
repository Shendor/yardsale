package com.yardsale.store.ws

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.util.Timeout
import com.yardsale.store.util.JsonUtil
import com.yardsale.store.validator.Validator.CommandViolated

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

trait ResponseHandlerRoute {

  implicit val timeout: Timeout = 10.seconds

  def handleResponse[T](ask: => Future[T]): Route = {
    onComplete(ask) {
      case Success(result) => result match {
        case CommandViolated(_, violations) => complete(HttpEntity(ContentTypes.`application/json`, JsonUtil.toJson(violations)))
        case other: Any => complete(HttpEntity(ContentTypes.`application/json`, JsonUtil.toJson(other)))
      }
      case Failure(ex) => complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, s"failed to process request: ${ex.getMessage}"))
    }
  }

  /* def handleResponse[T](ask: => Future[T]): Route = {
     handleResponse[T](ask, result => complete(HttpEntity(ContentTypes.`application/json`, JsonUtil.toJson(result))))
   }

   def handleResponse[T](ask: => Future[T], onSuccess: T => Route): Route = {
     onComplete(ask) {
       case Success(result) => {
         onSuccess(result)
       }
       case Failure(ex) => complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, s"failed to process request: ${ex.getMessage}"))
     }
   }*/

}
