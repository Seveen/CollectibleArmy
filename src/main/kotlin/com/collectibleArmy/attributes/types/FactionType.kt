package com.collectibleArmy.attributes.types

import org.hexworks.amethyst.api.Attribute

interface FactionType : Attribute {
    val name: String
}

abstract class BaseFactionType(override val name: String = "unknown") : FactionType

object BlueFaction : BaseFactionType(
    name = "blue faction")

object RedFaction : BaseFactionType(
    name = "red faction")

object NeutralFaction : BaseFactionType(
    name = "neutral faction"
)