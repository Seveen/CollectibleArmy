package com.collectibleArmy.systems.behaviors

import com.collectibleArmy.game.GameContext
import org.hexworks.amethyst.api.system.Behavior
import kotlin.reflect.KClass

//TODO: Replace it with Class.forName("").kotlin
class BehaviorsToStringMap {
    val map: HashMap<String, KClass<Behavior<GameContext>>> = hashMapOf(
        ForwardMover::class.simpleName!! to ForwardMover::class as KClass<Behavior<GameContext>>
    )
}

