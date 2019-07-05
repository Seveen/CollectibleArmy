package com.collectibleArmy

//package com.collectibleArmy.serializers
//
//import com.collectibleArmy.attributes.types.Hero
//import com.collectibleArmy.attributes.types.Soldier
//import kotlinx.serialization.*
//import kotlinx.serialization.internal.SerialClassDescImpl
//
//@Serializer(forClass = Hero::class)
//object HeroSerializer{
//
//    override val descriptor: SerialDescriptor = object : SerialClassDescImpl("Hero") {
//        init {
//            addElement("name")
//        }
//    }
//
//    override fun serialize(encoder: Encoder, obj: Hero) {
//        encoder.encodeString(obj.name)
//    }
//
//    override fun deserialize(decoder: Decoder): Hero {
//        return Hero(decoder.decodeString())
//    }
//}
//
//@Serializer(forClass = Soldier::class)
//object SoldierSerializer{
//
//    override val descriptor: SerialDescriptor = object : SerialClassDescImpl("Soldier") {
//        init {
//            addElement("name")
//        }
//    }
//
//    override fun serialize(encoder: Encoder, obj: Soldier) {
//        encoder.encodeString(obj.name)
//    }
//
//    override fun deserialize(decoder: Decoder): Soldier {
//        return Soldier(decoder.decodeString())
//    }
//}
