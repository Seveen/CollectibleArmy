package com.collectibleArmy.commands

import com.collectibleArmy.extensions.GameCommand
import com.collectibleArmy.extensions.GameEntity
import com.collectibleArmy.game.GameContext
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.Position

data class MoveTo(override val context: GameContext,
                  override val source: GameEntity<EntityType>,
                  val position: Position
) : GameCommand<EntityType>

