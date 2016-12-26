package com.yardsale.store.domain

import java.time.LocalDateTime

import com.fasterxml.jackson.annotation.JsonProperty

import scala.beans.BeanProperty

class StoreItem {

  @JsonProperty
  @BeanProperty
  var id: Long = _

  @JsonProperty
  @BeanProperty
  var name: String = _

  @JsonProperty
  @BeanProperty
  var description: String = _

  @JsonProperty
  @BeanProperty
  var price: Float = _

  @JsonProperty
  @BeanProperty
  var imagesUrl: Seq[String] = List.empty

  @JsonProperty
  @BeanProperty
  var postedDate: LocalDateTime = _

  @JsonProperty
  @BeanProperty
  var categories: Seq[String] = List.empty

  @JsonProperty
  @BeanProperty
  var filters: Seq[String] = List.empty

  @JsonProperty
  @BeanProperty
  var userId: Long = _

}
