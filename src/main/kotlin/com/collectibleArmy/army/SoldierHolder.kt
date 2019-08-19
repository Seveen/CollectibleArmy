package com.collectibleArmy.army

import com.collectibleArmy.army.templating.SoldierTemplate
import com.collectibleArmy.extensions.PositionSerializer
import kotlinx.serialization.*
import kotlinx.serialization.internal.SerialClassDescImpl
import org.hexworks.zircon.api.data.Position

@Serializable
class SoldierHolder(var soldier: SoldierTemplate,
                    initialPosition: Position,
                    initialAttackInitiative: Int,
                    initialDefendInitiative: Int,
                    initialForwardInitiative: Int,
                    initialRetreatInitiative: Int
): BaseUnitHolder(initialPosition,
                initialAttackInitiative,
                initialDefendInitiative,
                initialForwardInitiative,
                initialRetreatInitiative) {

    @Serializer(forClass = SoldierHolder::class)
    companion object : KSerializer<SoldierHolder> {
        override val descriptor: SerialDescriptor = object : SerialClassDescImpl( "SoldierHolder") {
            init {
                addElement("soldierName")
                addElement("position")
                addElement("attackInitiative")
                addElement("defendInitiative")
                addElement("forwardInitiative")
                addElement("retreatInitiative")
            }
        }

        override fun serialize(encoder: Encoder, obj: SoldierHolder) {
            val compositeOutput = encoder.beginStructure(descriptor)
            compositeOutput.encodeStringElement(descriptor, 0, obj.soldier.name)
            compositeOutput.encodeSerializableElement(descriptor, 1, PositionSerializer, obj.initialPosition)
            compositeOutput.encodeIntElement(descriptor, 2, obj.initialAttackInitiative)
            compositeOutput.encodeIntElement(descriptor, 3, obj.initialDefendInitiative)
            compositeOutput.encodeIntElement(descriptor, 4, obj.initialForwardInitiative)
            compositeOutput.encodeIntElement(descriptor, 5, obj.initialRetreatInitiative)
            compositeOutput.endStructure(descriptor)
        }

        override fun deserialize(decoder: Decoder): SoldierHolder {
            val dec: CompositeDecoder = decoder.beginStructure(descriptor)
            var soldier: SoldierTemplate? = null
            var soldierName: String? = null
            var position: Position? = null
            var attackInitiative: Int? = null
            var defendInitiative: Int? = null
            var forwardInitiative: Int? = null
            var retreatInitiative: Int? = null

            loop@ while (true) {
                when (val i = dec.decodeElementIndex(descriptor)) {
                    CompositeDecoder.READ_DONE -> break@loop
                    0 -> soldierName =  dec.decodeStringElement(descriptor, i)
                    1 -> position = dec.decodeSerializableElement(descriptor, i, PositionSerializer)
                    2 -> attackInitiative = dec.decodeIntElement(descriptor, i)
                    3 -> defendInitiative = dec.decodeIntElement(descriptor, i)
                    4 -> forwardInitiative = dec.decodeIntElement(descriptor, i)
                    5 -> retreatInitiative = dec.decodeIntElement(descriptor, i)
                    else -> throw SerializationException("Unknown index $i")
                }
            }
            dec.endStructure(descriptor)
            soldierName?.let {
                soldier = UnitsRepository.findSoldierTemplateByName(soldierName)
            }

            return SoldierHolder(
                soldier ?: throw MissingFieldException("soldier"),
                position ?: throw MissingFieldException("position"),
                attackInitiative ?: throw MissingFieldException("attackInitiative"),
                defendInitiative ?: throw MissingFieldException("defendInitiative"),
                forwardInitiative ?: throw MissingFieldException("forwardInitiative"),
                retreatInitiative ?: throw MissingFieldException("retreatInitiative")
            )
        }
    }
}