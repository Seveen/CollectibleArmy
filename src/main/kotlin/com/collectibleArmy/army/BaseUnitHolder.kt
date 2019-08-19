package com.collectibleArmy.army

import com.collectibleArmy.commands.globals.*
import org.hexworks.zircon.api.data.Position

open class BaseUnitHolder(override var initialPosition: Position,
                    override var initialAttackInitiative: Int,
                    override var initialDefendInitiative: Int,
                    override var initialForwardInitiative: Int,
                    override var initialRetreatInitiative: Int,
                    override var initialCastInitiative: Int
): UnitHolder {
    override fun getInitiative(command: GlobalCommand): Int {
        return when (command::class) {
            GlobalAttack::class -> initialAttackInitiative
            GlobalDefend::class -> initialDefendInitiative
            GlobalForward::class -> initialForwardInitiative
            GlobalRetreat::class -> initialRetreatInitiative
            GlobalCast::class -> initialCastInitiative
            else -> 0
        }
    }

    override fun setInitiative(command: GlobalCommand, value: Int) {
        when (command::class) {
            GlobalAttack::class -> initialAttackInitiative = value
            GlobalDefend::class -> initialDefendInitiative = value
            GlobalForward::class -> initialForwardInitiative = value
            GlobalRetreat::class -> initialRetreatInitiative = value
            GlobalCast::class -> initialCastInitiative = value
            else -> {
            }
        }
    }

    override fun incrementInitiative(command: GlobalCommand) {
        when (command::class) {
            GlobalAttack::class -> initialAttackInitiative++
            GlobalDefend::class -> initialDefendInitiative++
            GlobalForward::class -> initialForwardInitiative++
            GlobalRetreat::class -> initialRetreatInitiative++
            GlobalCast::class -> initialCastInitiative++
            else -> {
            }
        }
    }

    override fun decrementInitiative(command: GlobalCommand) {
        when (command::class) {
            GlobalAttack::class -> initialAttackInitiative--
            GlobalDefend::class -> initialDefendInitiative--
            GlobalForward::class -> initialForwardInitiative--
            GlobalRetreat::class -> initialRetreatInitiative--
            GlobalCast::class -> initialCastInitiative--
            else -> {
            }
        }
    }
}