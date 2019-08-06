package com.collectibleArmy.army

import com.collectibleArmy.army.templating.SoldierTemplate
import com.collectibleArmy.extensions.PositionSerializer
import kotlinx.serialization.*
import kotlinx.serialization.internal.SerialClassDescImpl
import org.hexworks.zircon.api.data.Position

@Serializable
class SoldierHolder(var soldier: SoldierTemplate,
                    var initialPosition: Position,
                    var initialInitiative: Int
) {
    @Serializer(forClass = SoldierHolder::class)
    companion object : KSerializer<SoldierHolder> {
        override val descriptor: SerialDescriptor = object : SerialClassDescImpl( "SoldierHolder") {
            init {
                addElement("soldierName")
                addElement("position")
                addElement("initiative")
            }
        }

        override fun serialize(encoder: Encoder, obj: SoldierHolder) {
            val compositeOutput = encoder.beginStructure(descriptor)
            compositeOutput.encodeStringElement(descriptor, 0, obj.soldier.name)
            compositeOutput.encodeSerializableElement(descriptor, 1, PositionSerializer, obj.initialPosition)
            compositeOutput.encodeIntElement(descriptor, 2, obj.initialInitiative)
            compositeOutput.endStructure(descriptor)
        }

        override fun deserialize(decoder: Decoder): SoldierHolder {
            val dec: CompositeDecoder = decoder.beginStructure(descriptor)
            var soldier: SoldierTemplate? = null
            var soldierName: String? = null
            var position: Position? = null
            var initiative: Int? = null

            loop@ while (true) {
                when (val i = dec.decodeElementIndex(descriptor)) {
                    CompositeDecoder.READ_DONE -> break@loop
                    0 -> soldierName =  dec.decodeStringElement(descriptor, i)
                    1 -> position = dec.decodeSerializableElement(descriptor, i, PositionSerializer)
                    2 -> initiative = dec.decodeIntElement(descriptor, i)
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
                initiative ?: throw MissingFieldException("initiative")
            )
        }
    }
}