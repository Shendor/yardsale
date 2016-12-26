package com.yardsale.store.util

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

import scala.reflect.ClassTag

object JsonUtil {
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  def toJson(value: Map[Symbol, Any]): String = {
    toJson(value map { case (k, v) => k.name -> v })
  }

  def toJson(value: Any): String = {
    mapper.writeValueAsString(value)
  }

  def toMap[T: ClassTag](json: String) = fromJson[Map[String, T]](json)

  def toObject[T: ClassTag](json: String): T = fromJson[T](json)

  def fromJson[T: ClassTag](json: String): T = {
    mapper.readValue(json, implicitly[ClassTag[T]].runtimeClass.asInstanceOf[Class[T]])
  }
}