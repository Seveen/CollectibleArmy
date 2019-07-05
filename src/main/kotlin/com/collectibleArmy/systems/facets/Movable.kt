package com.collectibleArmy.systems.facets

import com.collectibleArmy.attributes.types.Outside
import com.collectibleArmy.commands.Flee
import com.collectibleArmy.commands.MoveTo
import com.collectibleArmy.extensions.GameCommand
import com.collectibleArmy.extensions.faction
import com.collectibleArmy.extensions.tryActionsOn
import com.collectibleArmy.game.GameContext
import org.hexworks.amethyst.api.CommandResponse
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.extensions.map

object Movable : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>) =
        command.responseWhenCommandIs(MoveTo::class) { (context, entity, position) ->
            val area = context.area
            var result: Response = Pass
            area.fetchBlockAt(position.toPosition3D(0)).map { block ->
                result = if (block.isOccupied) {
                    val occupier = block.occupier.get()
                    if (occupier.type == Outside && occupier.faction == entity.faction) {
                        CommandResponse(
                            Flee(
                                context,
                                entity,
                                entity
                            )
                        )
                    } else {
                        entity.tryActionsOn(context, block.occupier.get())
                    }
                } else {
                    if (area.moveEntity(entity, position)) { Pass } else Consumed
                }
            }
            result
        }
}


