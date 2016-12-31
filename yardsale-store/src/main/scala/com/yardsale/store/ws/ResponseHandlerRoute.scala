package com.yardsale.store.ws

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.util.Timeout
import com.yardsale.store.service.{Event, QueryResult}
import com.yardsale.store.util.JsonUtil
import com.yardsale.store.validator.Validator.CommandViolated
import com.yardsale.store.validator.Violation

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

trait ResponseHandlerRoute {

  implicit val timeout: Timeout = 10.seconds

  def handleResponse[T](ask: => Future[T]): Route = {
    onComplete(ask) {
      case Success(result) => result match {
        case CommandViolated(_, violations) => complete(createHttpEntity(null, violations))

        case queryResult: QueryResult[Any] => complete(createHttpEntity(queryResult.result))

        case event: Event[Any] => complete(createHttpEntity(event.entity))

        case other: Any => complete(createHttpEntity(other))
      }
      case Failure(ex) => complete(StatusCodes.InternalServerError -> ex.getMessage)
    }
  }

  def createHttpEntity[T](response: T, violations: Iterable[Violation] = List.empty): ToResponseMarshallable = {
    HttpEntity(ContentTypes.`application/json`, JsonUtil.toJson(JsonResponse(response, violations)))
  }
}
