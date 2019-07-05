package com.collectibleArmy.extensions

import com.collectibleArmy.game.GameContext
import com.collectibleArmy.game.InitiativeEngine
import org.hexworks.amethyst.api.Engine
import org.hexworks.amethyst.api.Engines

fun <T: GameContext> Engines.newInitiativeEngine(): Engine<T> = InitiativeEngine()