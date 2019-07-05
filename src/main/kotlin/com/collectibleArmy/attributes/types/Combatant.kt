package com.collectibleArmy.attributes.types

import com.collectibleArmy.attributes.CombatStats
import com.collectibleArmy.extensions.GameEntity
import org.hexworks.amethyst.api.entity.EntityType

interface Combatant : EntityType

val GameEntity<Combatant>.combatStats: CombatStats
    get() = findAttribute(CombatStats::class).get()