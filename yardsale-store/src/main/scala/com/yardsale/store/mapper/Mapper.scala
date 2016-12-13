package com.yardsale.store.mapper

trait Mapper[Model, Entity] {

  def mapEntity(entity: Entity): Model

  def mapModel(model: Model): Entity

}
