package com.collectibleArmy.builders

import com.collectibleArmy.blocks.GameBlock

object GameBlockFactory {

    fun floor() = GameBlock(GameTileRepository.FLOOR)

    fun wall() = GameBlock.createWith(EntityFactory.newWall())

    fun redOutside() = GameBlock.createWith(EntityFactory.newRedOutside())
    fun blueOutside() = GameBlock.createWith(EntityFactory.newBlueOutside())

}
