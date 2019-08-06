package com.collectibleArmy.game

import com.collectibleArmy.commands.globals.GlobalCommand
import org.hexworks.amethyst.api.Context
import org.hexworks.zircon.api.screen.Screen

data class GameContext(val area: Area,
                       val screen: Screen,
                       val command: GlobalCommand
) : Context