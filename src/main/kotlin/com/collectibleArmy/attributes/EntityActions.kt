package com.collectibleArmy.attributes

import com.collectibleArmy.commands.EntityAction
import com.collectibleArmy.extensions.GameEntity
import com.collectibleArmy.game.GameContext
import org.hexworks.amethyst.api.Attribute
import org.hexworks.amethyst.api.entity.EntityType
import kotlin.reflect.KClass

class EntityActions(private vararg val actions: KClass<out EntityAction<out EntityType, out EntityType>>)
    : Attribute {

    fun createActionsFor(context: GameContext, source: GameEntity<EntityType>, target: GameEntity<EntityType>):
            Iterable<EntityAction<out EntityType, out EntityType>> {
        return actions.map {
            try {
                it.constructors.first().call(context, source, target)
            } catch (e: Exception) {
                throw IllegalArgumentException("Can't create EntityAction. Does it have the proper constructor?")
            }
        }
    }
}