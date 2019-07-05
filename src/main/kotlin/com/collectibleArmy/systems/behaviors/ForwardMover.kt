package com.collectibleArmy.systems.behaviors

import com.collectibleArmy.commands.MoveTo
import com.collectibleArmy.commands.globals.Forward
import com.collectibleArmy.commands.globals.Retreat
import com.collectibleArmy.extensions.GameEntity
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
        val currentPos = entity.position
        when (command::class) {
            Forward::class -> entity.moveTo(currentPos.withRelativeX(1), context)
            Retreat::class -> entity.moveTo(currentPos.withRelativeX(-1), context)
        }

        return true
    }

    private fun GameEntity<EntityType>.moveTo(position: Position, context: GameContext) {
        executeCommand(MoveTo(context, this, position))
    }
}
