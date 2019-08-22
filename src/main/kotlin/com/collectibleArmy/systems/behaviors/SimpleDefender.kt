package com.collectibleArmy.systems.behaviors

import com.collectibleArmy.extensions.GameEntity
import com.collectibleArmy.game.GameContext
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.logging.api.LoggerFactory

object SimpleDefender: GlobalBehavior() {
    private val logger = LoggerFactory.getLogger(this::class)

    override fun action(entity: GameEntity<EntityType>, context: GameContext) {
    }
}