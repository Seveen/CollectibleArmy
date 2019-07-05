package com.collectibleArmy.blocks

import com.collectibleArmy.builders.GameTileRepository
import com.collectibleArmy.extensions.GameEntity
import com.collectibleArmy.extensions.occupiesBlock
import com.collectibleArmy.extensions.tile
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.data.BlockSide
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BlockBase

class GameBlock(private var defaultTile: Tile = GameTileRepository.FLOOR,
                private val currentEntities: MutableList<GameEntity<EntityType>> = mutableListOf())
    : BlockBase<Tile>() {

    val isFloor: Boolean
        get() = defaultTile == GameTileRepository.FLOOR

    val isWall: Boolean
        get() = defaultTile == GameTileRepository.WALL

    val isEmptyFloor: Boolean
        get() = currentEntities.isEmpty()

    val entities: Iterable<GameEntity<EntityType>>
        get() = currentEntities.toList()

    val occupier: Maybe<GameEntity<EntityType>>
        get() = Maybe.ofNullable(currentEntities.firstOrNull { it.occupiesBlock})

    val isOccupied: Boolean
        get() = occupier.isPresent

    override val layers: MutableList<Tile>
        get() {
            val entityTiles = currentEntities.map { it.tile }
            val tile = when {
                entityTiles.isNotEmpty() -> entityTiles.first()
                else -> defaultTile
            }
            return mutableListOf(tile)
        }

    fun addEntity(entity: GameEntity<EntityType>) {
        currentEntities.add(entity)
    }

    fun removeEntity(entity: GameEntity<EntityType>) {
        currentEntities.remove(entity)
    }

    fun clearEntities() {
        currentEntities.clear()
    }

    override fun fetchSide(side: BlockSide): Tile {
        return GameTileRepository.EMPTY
    }

    companion object {
        fun createWith(entity: GameEntity<EntityType>) = GameBlock(
            currentEntities = mutableListOf(entity)
        )
    }
}
