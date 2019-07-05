package com.collectibleArmy.game

import com.collectibleArmy.blocks.GameBlock
import com.collectibleArmy.builders.GameBlockFactory
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size

class AreaBuilder(private val areaSize: Size) {
    private val width = areaSize.width
    private val height = areaSize.height
    private var blocks: MutableMap<Position, GameBlock> = mutableMapOf()

    fun makeArena(): AreaBuilder {
        return makeWalls()
    }

    fun build(visibleSize: Size): Area = Area(blocks, visibleSize, areaSize)

    private fun makeWalls(): AreaBuilder {
        forAllPositions { pos ->
            val isEdge = pos.y == 0 || pos.y == areaSize.height - 1
            val isRedOutside = pos.x == areaSize.width - 1
            val isBlueOutside = pos.x == 0
            when {
                isEdge -> blocks[pos] = GameBlockFactory.wall()
                isRedOutside -> blocks[pos] = GameBlockFactory.redOutside()
                isBlueOutside -> blocks[pos] = GameBlockFactory.blueOutside()
                else -> blocks[pos] = GameBlockFactory.floor()
            }
        }
        return this
    }

    private fun forAllPositions(fn: (Position) -> Unit) {
        areaSize.fetchPositions().forEach(fn)
    }
}