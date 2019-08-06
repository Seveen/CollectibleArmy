package com.collectibleArmy.game

import com.collectibleArmy.army.Army
import com.collectibleArmy.attributes.types.*
import com.collectibleArmy.blocks.GameBlock
import com.collectibleArmy.builders.EntityFactory.buildHeroFromTemplate
import com.collectibleArmy.builders.EntityFactory.buildSoldierFromTemplate
import com.collectibleArmy.builders.GameBlockFactory
import com.collectibleArmy.commands.globals.GlobalCommand
import com.collectibleArmy.extensions.*
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

class Area(val startingBlocks: Map<Position, GameBlock>,
           visibleSize: Size,
           actualSize: Size) : GameArea<Tile, GameBlock> by GameAreaBuilder.newBuilder<Tile, GameBlock>()
    .withVisibleSize(Size3D.from2DSize(visibleSize, 1))
    .withActualSize(Size3D.from2DSize(actualSize, 1))
    .withDefaultBlock(DEFAULT_BLOCK)
    .withLayersPerBlock(1)
    .build() {

    private val engine: InitiativeEngine<GameContext> = Engines.newInitiativeEngine()

    init {
        initStartingBlocks()
    }

    fun addEntity(entity: GameEntity<EntityType>, position: Position) {
        entity.position = position
        engine.addEntity(entity)
        fetchBlockAt(Position3D.from2DPosition(position,0)).map {
            it.addEntity(entity)
        }
    }

    fun addEntity(entity: GameEntity<EntityType>, position: Position, initiative: Int) {
        entity.position = position
        entity.initiative = initiative
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

    private fun initStartingBlocks() {
        startingBlocks.forEach { (pos, block) ->
            setBlockAt(Position3D.from2DPosition(pos, 0), block)
            block.entities.forEach { entity ->
                engine.addEntity(entity)
                entity.position = pos
            }
        }
    }

    fun rebuildAreaWithArmies(blueArmy: Army?, redArmy: Army?) {
        fetchBlocks().forEach {
            val block = it.block
            if (block.isOccupied) {
                block.occupier.get().let { entity ->
                    entity.whenTypeIs<Combatant> {
                        if (entity.faction != NeutralFaction) {
                            block.removeEntity(entity)
                            engine.removeEntity(entity)
                            entity.position = Position.unknown()
                        }
                    }
                }
            }
        }

        blueArmy?.let {
            loadArmy(it, BlueFaction)
        }

        redArmy?.let {
            loadArmy(it, RedFaction)
        }

    }

    fun loadArmy(army: Army, faction: FactionType) {
        val hero = buildHeroFromTemplate(army.heroHolder.hero, faction)
        hero.initiative = army.heroHolder.initialInitiative
        addEntity(hero, army.heroHolder.initialPosition)

        army.troopHolders.forEach { holder ->
            val soldier = buildSoldierFromTemplate(holder.soldier, faction)
            addEntity(soldier, holder.initialPosition, holder.initialInitiative)
        }
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