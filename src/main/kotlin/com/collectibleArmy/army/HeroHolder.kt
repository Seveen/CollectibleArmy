package com.collectibleArmy.army

import com.collectibleArmy.army.templating.HeroTemplate
import com.collectibleArmy.commands.globals.*
import com.collectibleArmy.extensions.PositionSerializer
import kotlinx.serialization.*
import kotlinx.serialization.internal.SerialClassDescImpl
import org.hexworks.zircon.api.data.Position

@Serializable
class HeroHolder(var hero: HeroTemplate,
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

    @Serializer(forClass = HeroHolder::class)
    companion object : KSerializer<HeroHolder> {
        override val descriptor: SerialDescriptor = object : SerialClassDescImpl( "HeroHolder") {
            init {
                addElement("heroName")
                addElement("position")
                addElement("attackInitiative")
                addElement("defendInitiative")
                addElement("forwardInitiative")
                addElement("retreatInitiative")
            }
        }

        override fun serialize(encoder: Encoder, obj: HeroHolder) {
            val compositeOutput = encoder.beginStructure(descriptor)
            compositeOutput.encodeStringElement(descriptor, 0, obj.hero.name)
            compositeOutput.encodeSerializableElement(descriptor, 1, PositionSerializer, obj.initialPosition)
            compositeOutput.encodeIntElement(descriptor, 2, obj.initialAttackInitiative)
            compositeOutput.encodeIntElement(descriptor, 3, obj.initialDefendInitiative)
            compositeOutput.encodeIntElement(descriptor, 4, obj.initialForwardInitiative)
            compositeOutput.encodeIntElement(descriptor, 5, obj.initialRetreatInitiative)
            compositeOutput.endStructure(descriptor)
        }

        override fun deserialize(decoder: Decoder): HeroHolder {
            val dec: CompositeDecoder = decoder.beginStructure(descriptor)
            var hero: HeroTemplate? = null
            var heroName: String? = null
            var position: Position? = null
            var attackInitiative: Int? = null
            var defendInitiative: Int? = null
            var forwardInitiative: Int? = null
            var retreatInitiative: Int? = null

            loop@ while (true) {
                when (val i = dec.decodeElementIndex(descriptor)) {
                    CompositeDecoder.READ_DONE -> break@loop
                    0 -> heroName =  dec.decodeStringElement(descriptor, i)
                    1 -> position = dec.decodeSerializableElement(descriptor, i, PositionSerializer)
                    2 -> attackInitiative = dec.decodeIntElement(descriptor, i)
                    3 -> defendInitiative = dec.decodeIntElement(descriptor, i)
                    4 -> forwardInitiative = dec.decodeIntElement(descriptor, i)
                    5 -> retreatInitiative = dec.decodeIntElement(descriptor, i)
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
                attackInitiative ?: throw MissingFieldException("attackInitiative"),
                defendInitiative ?: throw MissingFieldException("defendInitiative"),
                forwardInitiative ?: throw MissingFieldException("forwardInitiative"),
                retreatInitiative ?: throw MissingFieldException("retreatInitiative")
            )
        }
    }
}