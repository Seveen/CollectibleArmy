package com.collectibleArmy.attributes

import org.hexworks.amethyst.api.Attribute
import kotlin.reflect.KClass

//TODO: Replace it with Class.forName("").kotlin
class AttributeToStringMap {
    val map: HashMap<String, KClass<Attribute>> = hashMapOf(
        BlockOccupier::class.simpleName!! to BlockOccupier::class as KClass<Attribute>,
        EntityPosition::class.simpleName!! to EntityPosition::class as KClass<Attribute>,
        EntityTile::class.simpleName!! to EntityTile::class as KClass<Attribute>,
        Faction::class.simpleName!! to Faction::class as KClass<Attribute>
    )
}

