package com.collectibleArmy.attributes

import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.zircon.api.data.Position

class EntityPosition(initialPosition: Position = Position.unknown()) : Attribute {
    private val positionProperty = createPropertyFrom(initialPosition)

    var position: Position by positionProperty.asDelegate()
}