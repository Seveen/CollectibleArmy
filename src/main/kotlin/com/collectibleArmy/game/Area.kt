package com.collectibleArmy.game

import com.collectibleArmy.blocks.GameBlock
import com.collectibleArmy.builders.GameBlockFactory
import com.collectibleArmy.command.globals.GlobalCommand
import com.collectibleArmy.extensions.GameEntity
import com.collectibleArmy.extensions.newInitiativeEngine
import com.collectibleArmy.extensions.position
import org.hexworks.amethyst.api.Engine
import org.hexworks.amethyst.api.Engines
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.game.GameArea
import org.hexworks.zircon.api.screen.Screen

class Area(startingBlocks: Map<Position, GameBlock>,
           visibleSize: Size,
           actualSize: Size) : GameArea<Tile, GameBlock> by GameAreaBuilder.newBuilder<Tile, GameBlock>()
    .withVisibleSize(Size3D.from2DSize(visibleSize, 1))
    .withActualSize(Size3D.from2DSize(actualSize, 1))
    .withDefaultBlock(DEFAULT_BLOCK)
    .withLayersPerBlock(1)
    .build() {

    private val engine: Engine<GameContext> = Engines.newInitiativeEngine()

    init {
        startingBlocks.forEach { (pos, block) ->
            setBlockAt(Position3D.from2DPosition(pos, 0), block)
            block.entities.forEach { entity ->
                engine.addEntity(entity)
                entity.position = pos
            }
        }
    }

    fun addEntity(entity: GameEntity<EntityType>, position: Position) {
        entity.position = position
        engine.addEntity(entity)
        fetchBlockAt(Position3D.from2DPosition(position,0)).map {
            it.addEntity(entity)
        }
    }

    fun removeEntity(entity: GameEntity<EntityType>) {
        fetchBlockAt(Position3D.from2DPosition(entity.position,0)).map {
            it.removeEntity(entity)
        }
        engine.removeEntity(entity)
        entity.position = Position.unknown()
    }

    fun addWorldEntity(entity: Entity<EntityType, GameContext>) {
        engine.addEntity(entity)
    }

    fun update(screen: Screen, command: GlobalCommand, game: Game) {
        engine.update(GameContext(
            area = this,
            screen = screen,
            command = command
        ))
    }

    fun moveEntity(entity: GameEntity<EntityType>, position: Position): Boolean {
        var success = false
        val oldBlock = fetchBlockAt(Position3D.from2DPosition(entity.position,0))
        val newBlock = fetchBlockAt(Position3D.from2DPosition(position, 0))

        if (bothBlocksPresent(oldBlock, newBlock)) {
            success = true
            oldBlock.get().removeEntity(entity)
            entity.position = position
            newBlock.get().addEntity(entity)
        }
        return success
    }

    private fun bothBlocksPresent(oldBlock: Maybe<GameBlock>, newBlock: Maybe<GameBlock>) =
        oldBlock.isPresent && newBlock.isPresent

    fun screenToWorldPosition(screenPosition: Position, screenOffset: Position) : Position {
        val worldOffset = this.visibleOffset().to2DPosition()
        return screenPosition.plus(worldOffset).minus(screenOffset)
    }

    companion object {
        private val DEFAULT_BLOCK = GameBlockFactory.floor()
    }
}