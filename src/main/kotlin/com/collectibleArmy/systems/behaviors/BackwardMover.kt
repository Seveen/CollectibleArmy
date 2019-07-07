package com.collectibleArmy.systems.behaviors

import com.collectibleArmy.commands.globals.GlobalRetreat
import com.collectibleArmy.extensions.GameEntity
import com.collectibleArmy.extensions.backward
import com.collectibleArmy.extensions.faction
import com.collectibleArmy.extensions.position
import com.collectibleArmy.game.GameContext
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.Position

object BackwardMover : BaseBehavior<GameContext>() {
    private val logger = LoggerFactory.getLogger(this::class)

    override fun update(entity: GameEntity<EntityType>, context: GameContext): Boolean {
        val (_, _, command) = context
        when (command::class) {
            GlobalRetreat::class -> entity.moveTo(entity.position.backward(entity.faction), context)
        }

        return true
    }

    private fun GameEntity<EntityType>.moveTo(position: Position, context: GameContext) {
        executeCommand(com.collectibleArmy.commands.MoveTo(context, this, position))
    }
}