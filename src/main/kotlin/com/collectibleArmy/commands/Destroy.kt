package com.collectibleArmy.commands

import com.collectibleArmy.extensions.GameCommand
import com.collectibleArmy.extensions.GameEntity
import com.collectibleArmy.game.GameContext
import org.hexworks.amethyst.api.entity.EntityType

data class Destroy(override val context: GameContext,
                   override val source: GameEntity<EntityType>,
                   val target: GameEntity<EntityType>,
                   val cause: String = "natural cause") : GameCommand<EntityType>