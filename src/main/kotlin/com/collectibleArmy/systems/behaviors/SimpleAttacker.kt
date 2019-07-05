package com.collectibleArmy.systems.behaviors

import com.collectibleArmy.attributes.types.Combatant
import com.collectibleArmy.commands.Attack
import com.collectibleArmy.commands.globals.GlobalAttack
import com.collectibleArmy.extensions.*
import com.collectibleArmy.game.GameContext
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.Position

object SimpleAttacker : BaseBehavior<GameContext>() {
    private val logger = LoggerFactory.getLogger(this::class)

    override fun update(entity: GameEntity<EntityType>, context: GameContext): Boolean {
        entity.whenTypeIs<Combatant> {
            val (_, _, command) = context
            when (command::class) {
                GlobalAttack::class -> {
                    it.tryToAttack(entity.position.forward(entity.faction), context)
                }
            }
        }

        return true
    }

    private fun GameEntity<Combatant>.tryToAttack(position: Position, context: GameContext) {
        val area = context.area
        area.fetchBlockAt(position.toPosition3D(0)).map { block ->
            if (block.isOccupied) {
                val occupier = block.occupier.get()
                occupier.whenTypeIs<Combatant> {
                    executeCommand(Attack(context, this, it))
                }
            }
        }
    }
}