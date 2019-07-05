package com.collectibleArmy.systems.facets

import com.collectibleArmy.game.GameContext
import org.hexworks.amethyst.api.system.Facet
import kotlin.reflect.KClass

//TODO: Replace it with Class.forName("").kotlin
class FacetsToStringMap {
    val map: HashMap<String, KClass<Facet<GameContext>>> = hashMapOf(
        Movable::class.simpleName!! to Movable::class as KClass<Facet<GameContext>>
    )
}
