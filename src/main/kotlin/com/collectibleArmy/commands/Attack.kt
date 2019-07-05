package com.collectibleArmy.commands

import com.collectibleArmy.attributes.types.Combatant
import com.collectibleArmy.extensions.GameEntity
import com.collectibleArmy.game.GameContext

data class Attack(override val context: GameContext,
                  override val source: GameEntity<Combatant>,
                  override val target: GameEntity<Combatant>) : EntityAction<Combatant, Combatant>