package com.collectibleArmy.systems.facets

import com.collectibleArmy.attributes.types.combatStats
import com.collectibleArmy.commands.Attack
import com.collectibleArmy.commands.Destroy
import com.collectibleArmy.extensions.*
import com.collectibleArmy.functions.logGameEvent
import com.collectibleArmy.game.GameContext
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import kotlin.math.max

object Attackable : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>) =
        command.responseWhenCommandIs(Attack::class) { (context, attacker, target) ->
            var allied = false
            if (attacker.hasFaction && target.hasFaction) {
                allied = attacker.faction == target.faction
            }
            if (allied.not()) {
                val damage = max(0, attacker.attackValue - target.defenseValue)
                val finalDamage = (Math.random() * damage).toInt() + 1
                target.combatStats.hp -= finalDamage

                logGameEvent("The $attacker hits the $target for $finalDamage!")

                target.whenHasNoHealthLeft {
                    target.executeCommand(
                        Destroy(
                            context = context,
                            source = attacker,
                            target = target,
                            cause = "a blow to the head"
                        )
                    )
                }
                Consumed
            } else Pass
        }
}