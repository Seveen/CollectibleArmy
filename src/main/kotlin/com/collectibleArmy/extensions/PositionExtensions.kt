package com.collectibleArmy.extensions

import com.collectibleArmy.attributes.types.BlueFaction
import com.collectibleArmy.attributes.types.FactionType
import com.collectibleArmy.attributes.types.RedFaction
import org.hexworks.zircon.api.data.Position
import kotlin.math.abs

fun Position.getDistanceFrom(other: Position): Int {
    val delta = this.minus(other)
    return abs(delta.x) + abs(delta.y)
}

fun Position.forward(side: FactionType): Position {
    return when(side) {
        BlueFaction -> this.withRelativeX(1)
        RedFaction -> this.withRelativeX(-1)
        else -> this
    }
}

fun Position.backward(side: FactionType): Position {
    return when(side) {
        BlueFaction -> this.withRelativeX(-1)
        RedFaction -> this.withRelativeX(1)
        else -> this
    }
}

fun Position.isWithin(lower: Position, higher: Position) : Boolean{
    return lower.x <= x &&
            lower.y <= y &&
            x <= higher.x &&
            y <= higher.y
}