package com.yardsale.store.util

import akka.http.scaladsl.marshalling.{Marshaller, _}
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.unmarshalling.{Unmarshaller, _}

import scala.reflect.ClassTag

trait JacksonJsonSupport {

  implicit def jacksonJsonUnmarshaller[T: ClassTag]: FromEntityUnmarshaller[T] = {
    Unmarshaller.byteStringUnmarshaller.forContentTypes(`application/json`).mapWithCharset { (data, charset) ⇒
      JsonUtil.toObject[T](data.decodeString(charset.nioCharset.name))
    }
  }

  implicit def jacksonJsonMarshaller[T <: AnyRef]: ToEntityMarshaller[T] = {
    Marshaller.withFixedContentType(`application/json`) { any =>
      HttpEntity(`application/json`, JsonUtil.toJson(any))
    }
  }

}
