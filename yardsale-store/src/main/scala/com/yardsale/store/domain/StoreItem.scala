package com.yardsale.store.domain

import java.time.LocalDateTime

import scala.beans.BeanProperty

class StoreItem {

  @BeanProperty
  var id: Long = _

  @BeanProperty
  var name: String = _

  @BeanProperty
  var description: String = _

  @BeanProperty
  var price: Float = _

  @BeanProperty
  var imagesUrl: Seq[String] = List.empty

  @BeanProperty
  var postedDate: LocalDateTime = _

  @BeanProperty
  var categories: Seq[String] = List.empty

  @BeanProperty
  var filters: Seq[String] = List.empty

  @BeanProperty
  var userId: Long = _

}
