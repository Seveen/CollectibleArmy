package com.collectibleArmy.army

import com.collectibleArmy.commands.globals.GlobalCommand
import org.hexworks.zircon.api.data.Position

interface UnitHolder {
    var initialPosition: Position
    var initialAttackInitiative: Int
    var initialDefendInitiative: Int
    var initialForwardInitiative: Int
    var initialRetreatInitiative: Int
    var initialCastInitiative: Int

    fun getInitiative(command: GlobalCommand) : Int
    fun setInitiative(command: GlobalCommand, value: Int)
    fun incrementInitiative(command: GlobalCommand)
    fun decrementInitiative(command: GlobalCommand)
}
