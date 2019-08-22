package com.collectibleArmy.systems.behaviors

import com.collectibleArmy.commands.globals.GlobalCommand
import com.collectibleArmy.extensions.GameEntity
import com.collectibleArmy.game.GameContext
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.EntityType

abstract class GlobalBehavior : BaseBehavior<GameContext>(){
    private lateinit var respondedCommand: GlobalCommand

    fun setResponseCommand(command: GlobalCommand) {
        respondedCommand = command
    }

    override fun update(entity: GameEntity<EntityType>, context: GameContext): Boolean {
        val (_, _, command) = context
        when (command::class) {
            respondedCommand::class -> action(entity, context)
        }

        return true
    }

    abstract fun action(entity: GameEntity<EntityType>, context: GameContext)
}