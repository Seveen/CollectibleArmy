package com.collectibleArmy.attributes.types

import org.hexworks.amethyst.api.base.BaseEntityType

//@Serializable(with = HeroSerializer::class)
class Hero(override val name: String): BaseEntityType()

//@Serializable(with = SoldierSerializer::class)
class Soldier(override val name: String): BaseEntityType()

object Wall : BaseEntityType(
    name = "wall")

object Outside : BaseEntityType(
    name = "outside"
)