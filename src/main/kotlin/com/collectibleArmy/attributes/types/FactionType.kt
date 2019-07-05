package com.collectibleArmy.attributes.types

import org.hexworks.amethyst.api.Attribute

interface FactionType : Attribute {
    val name: String
}

abstract class BaseFactionType(override val name: String = "unknown") : FactionType

object PlayerFaction : BaseFactionType(
    name = "player faction")

object EnemyFaction : BaseFactionType(
    name = "enemy faction")

object NeutralFaction : BaseFactionType(
    name = "neutral faction"
)