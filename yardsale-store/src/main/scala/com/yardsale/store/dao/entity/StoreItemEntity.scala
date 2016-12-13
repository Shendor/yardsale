package com.yardsale.store.dao.entity

import java.time.LocalDateTime

import scala.beans.BeanProperty

class StoreItemEntity {

  @BeanProperty
  var id : Long = _

  @BeanProperty
  var name : String = _

  @BeanProperty
  var description : String = _

  @BeanProperty
  var price : Float = _

  @BeanProperty
  var imagesUrl : String = _

  @BeanProperty
  var postedDate : LocalDateTime = _

  @BeanProperty
  var category : String = _

  @BeanProperty
  var filter : String = _

  @BeanProperty
  var userId : Long = _
}
