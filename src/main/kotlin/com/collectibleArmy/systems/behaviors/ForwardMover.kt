package com.collectibleArmy.systems.behaviors

import com.collectibleArmy.commands.MoveTo
import com.collectibleArmy.commands.globals.GlobalForward
import com.collectibleArmy.extensions.GameEntity
import com.collectibleArmy.extensions.faction
import com.collectibleArmy.extensions.forward
import com.collectibleArmy.extensions.position
import com.collectibleArmy.game.GameContext
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.Position

object ForwardMover : BaseBehavior<GameContext>() {
    private val logger = LoggerFactory.getLogger(this::class)

    override fun update(entity: GameEntity<EntityType>, context: GameContext): Boolean {
        val (_, _, command) = context
        when (command::class) {
            GlobalForward::class -> {
                entity.moveTo(entity.position.forward(entity.faction), context)
            }
        }

        return true
    }

    private fun GameEntity<EntityType>.moveTo(position: Position, context: GameContext) {
        executeCommand(MoveTo(context, this, position))
    }
}
