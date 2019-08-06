package com.collectibleArmy.extensions

import com.collectibleArmy.attributes.types.BlueFaction
import com.collectibleArmy.attributes.types.FactionType
import com.collectibleArmy.attributes.types.RedFaction
import kotlinx.serialization.*
import kotlinx.serialization.internal.SerialClassDescImpl
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

@Serializer(forClass = Position::class)
object PositionSerializer: KSerializer<Position> {
    override val descriptor: SerialDescriptor = object : SerialClassDescImpl("Position") {
        init {
            addElement("x")
            addElement("y")
        }
    }

    override fun serialize(encoder: Encoder, obj: Position) {
        val compositeOutput = encoder.beginStructure(descriptor)
        compositeOutput.encodeIntElement(descriptor, 0, obj.x)
        compositeOutput.encodeIntElement(descriptor, 1, obj.y)
        compositeOutput.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): Position {
        val dec: CompositeDecoder = decoder.beginStructure(descriptor)
        var x: Int? = null
        var y: Int? = null

        loop@ while (true) {
            when (val i = dec.decodeElementIndex(descriptor)) {
                CompositeDecoder.READ_DONE -> break@loop
                0 -> x = dec.decodeIntElement(descriptor, i)
                1 -> y = dec.decodeIntElement(descriptor, i)
                else -> throw SerializationException("Unknown index $i")
            }
        }
        dec.endStructure(descriptor)

        val goodX = x ?: throw MissingFieldException("x")
        val goodY = y ?: throw MissingFieldException("y")

        return Position.create(goodX, goodY)
    }
}