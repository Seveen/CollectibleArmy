package com.collectibleArmy.builders

import com.collectibleArmy.blocks.GameBlock

object GameBlockFactory {

    fun floor() = GameBlock(GameTileRepository.FLOOR)

    fun wall() = GameBlock.createWith(EntityFactory.newWall())

    fun outside() = GameBlock.createWith(EntityFactory.newOutside())
}
