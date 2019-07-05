package com.collectibleArmy.systems.facets

import com.collectibleArmy.commands.Flee
import com.collectibleArmy.extensions.GameCommand
import com.collectibleArmy.functions.logGameEvent
import com.collectibleArmy.game.GameContext
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object Fleeable : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>) =
        command.responseWhenCommandIs(Flee::class) {(context, _, target) ->
            context.area.removeEntity(target)
            logGameEvent("$target flees from the battlefield!")
            Consumed
        }
}