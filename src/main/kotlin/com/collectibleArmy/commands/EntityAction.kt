package com.collectibleArmy.commands

import com.collectibleArmy.extensions.GameCommand
import com.collectibleArmy.extensions.GameEntity
import org.hexworks.amethyst.api.entity.EntityType

interface EntityAction<S : EntityType, T : EntityType> : GameCommand<S> {

    val target: GameEntity<T>

    operator fun component1() = context
    operator fun component2() = source
    operator fun component3() = target
}