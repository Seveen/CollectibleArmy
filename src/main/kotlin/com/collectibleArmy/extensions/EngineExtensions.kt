package com.collectibleArmy.extensions

import com.collectibleArmy.game.GameContext
import com.collectibleArmy.game.InitiativeEngine
import org.hexworks.amethyst.api.Engines

fun Engines.newInitiativeEngine(): InitiativeEngine<GameContext> = InitiativeEngine()