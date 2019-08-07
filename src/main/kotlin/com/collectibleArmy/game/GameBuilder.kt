package com.collectibleArmy.game

import com.collectibleArmy.GameConfig.ARENA_HEIGHT
import com.collectibleArmy.GameConfig.ARENA_WIDTH
import com.collectibleArmy.army.Army
import com.collectibleArmy.attributes.types.BlueFaction
import com.collectibleArmy.attributes.types.RedFaction
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.data.Size

class GameBuilder(val worldSize: Size) {

    private val visibleSize = worldSize

    private var blueArmy: Army? = null
    private var redArmy: Army? = null

    private val area = AreaBuilder(worldSize)
        .makeArena()
        .build(visibleSize = visibleSize)

    fun withBlueArmy(army: Army): GameBuilder {
        blueArmy = army
        return this
    }

    fun withRedArmy(army: Army): GameBuilder {
        redArmy = army
        return this
    }

    fun buildGame(): Game {
        prepareWorld()

        blueArmy?.let {
            area.loadArmy(it, BlueFaction)
        }

        redArmy?.let {
            area.loadArmy(it, RedFaction)
        }

        return Game.create(
            area = area
        )
    }

    private fun prepareWorld() = also {
        area.scrollUpBy(area.actualSize().zLength)
    }

    companion object {

        fun defaultGame() = GameBuilder(
            worldSize = Sizes.create(ARENA_WIDTH, ARENA_HEIGHT))
            .buildGame()

        fun defaultEditorGame() = GameBuilder(
            worldSize = Sizes.create(ARENA_WIDTH,ARENA_HEIGHT))
            .buildGame()

        fun defaultSelectGame() = GameBuilder(
            worldSize = Sizes.create(ARENA_HEIGHT,ARENA_HEIGHT))
            .buildGame()

        fun editorGame(army: Army) = GameBuilder(
            worldSize = Sizes.create(ARENA_WIDTH,ARENA_HEIGHT))
            .withBlueArmy(army)
            .buildGame()
    }
}