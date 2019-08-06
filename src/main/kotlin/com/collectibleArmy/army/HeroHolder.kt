package com.collectibleArmy.army

import com.collectibleArmy.army.templating.HeroTemplate
import com.collectibleArmy.extensions.PositionSerializer
import kotlinx.serialization.*
import kotlinx.serialization.internal.SerialClassDescImpl
import org.hexworks.zircon.api.data.Position

@Serializable
class HeroHolder(var hero: HeroTemplate,
                 override var initialPosition: Position,
                 override var initialInitiative: Int
): UnitHolder {
    @Serializer(forClass = HeroHolder::class)
    companion object : KSerializer<HeroHolder> {
        override val descriptor: SerialDescriptor = object : SerialClassDescImpl( "HeroHolder") {
            init {
                addElement("heroName")
                addElement("position")
                addElement("initiative")
            }
        }

        override fun serialize(encoder: Encoder, obj: HeroHolder) {
            val compositeOutput = encoder.beginStructure(descriptor)
            compositeOutput.encodeStringElement(descriptor, 0, obj.hero.name)
            compositeOutput.encodeSerializableElement(descriptor, 1, PositionSerializer, obj.initialPosition)
            compositeOutput.encodeIntElement(descriptor, 2, obj.initialInitiative)
            compositeOutput.endStructure(descriptor)
        }

        override fun deserialize(decoder: Decoder): HeroHolder {
            val dec: CompositeDecoder = decoder.beginStructure(descriptor)
            var hero: HeroTemplate? = null
            var heroName: String? = null
            var position: Position? = null
            var initiative: Int? = null

            loop@ while (true) {
                when (val i = dec.decodeElementIndex(descriptor)) {
                    CompositeDecoder.READ_DONE -> break@loop
                    0 -> heroName =  dec.decodeStringElement(descriptor, i)
                    1 -> position = dec.decodeSerializableElement(descriptor, i, PositionSerializer)
                    2 -> initiative = dec.decodeIntElement(descriptor, i)
                    else -> throw SerializationException("Unknown index $i")
                }
            }
            dec.endStructure(descriptor)
            heroName?.let {
                hero = UnitsRepository.findHeroTemplateByName(heroName)
            }

            return HeroHolder(
                hero ?: throw MissingFieldException("hero"),
                position ?: throw MissingFieldException("position"),
                initiative ?: throw MissingFieldException("initiative")
            )
        }
    }
}