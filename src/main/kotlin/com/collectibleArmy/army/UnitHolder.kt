package com.collectibleArmy.army

import org.hexworks.zircon.api.data.Position

interface UnitHolder {
    var initialPosition: Position
    var initialInitiative: Int
}
