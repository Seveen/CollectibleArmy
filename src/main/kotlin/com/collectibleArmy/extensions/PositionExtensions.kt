package com.collectibleArmy.extensions

import org.hexworks.zircon.api.data.Position
import kotlin.math.abs

fun Position.getDistanceFrom(other: Position): Int {
    val delta = this.minus(other)
    return abs(delta.x) + abs(delta.y)
}