package com.collectibleArmy.systems.facets

import com.collectibleArmy.commands.Destroy
import com.collectibleArmy.extensions.GameCommand
import com.collectibleArmy.functions.logGameEvent
import com.collectibleArmy.game.GameContext
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object Destructible : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>) =
        command.responseWhenCommandIs(Destroy::class) { (context, attacker, target, cause) ->
            var result: Response = Consumed
            context.area.removeEntity(target)

            logGameEvent("$target dies after receiving $cause.")
            result
        }
}