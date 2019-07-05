package com.collectibleArmy.game

import com.collectibleArmy.army.Army
import com.collectibleArmy.army.ArmyBuilder
import com.collectibleArmy.army.templating.*
import com.collectibleArmy.attributes.types.BlueFaction
import com.collectibleArmy.attributes.types.RedFaction
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.data.Position
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
            area.loadArmy(it)
        }

        redArmy?.let {
            area.loadArmy(it)
        }

        return Game.create(
            area = area
        )
    }

    private fun prepareWorld() = also {
        area.scrollUpBy(area.actualSize().zLength)
    }

    companion object {

        //Todo: Take some time to reimplement that template system
//        val defaultHeroTemplate = HeroTemplate(
////            name = "Dumbass",
////            attributes = listOf(
////                "EntityPosition",
////                "BlockOccupier",
////                "EntityTile"
////            ),
////            behaviors = listOf(
////                "ForwardMover"
////            ),
////            facets = listOf(
////                "Movable"
////            )
////        )

        private val defaultPlayerArmy = ArmyBuilder()
            .withHero(
                HeroTemplate(
                    name = "Hero",
                    tile = TileTemplate(
                        char = '@',
                        foregroundColor = "#440000",
                        backGroundColor = "#000000"
                    ),
                    stats = StatsTemplate(
                        attack = 2,
                        defense = 1,
                        hp = 10
                    ),
                    behaviors = BehaviorsTemplate(
                        forward = "ForwardMover",
                        backward = "",
                        attack = "SimpleAttacker",
                        defend = ""
                    )
                ),
                Position.create(3,3),
                1)
            .withSoldier(
                SoldierTemplate(
                    name = "Soldier",
                    tile = TileTemplate(
                        char = 'S',
                        foregroundColor = "#440000",
                        backGroundColor = "#000000"
                    ),
                    stats = StatsTemplate(
                        attack = 2,
                        defense = 1,
                        hp = 10
                    ),
                    behaviors = BehaviorsTemplate(
                        forward = "ForwardMover",
                        backward = "",
                        attack = "SimpleAttacker",
                        defend = ""
                    )
                ),
                Position.create(3,2),
                2
            )
            .withSoldier(
                SoldierTemplate(
                    name = "Soldier",
                    tile = TileTemplate(
                        char = 'S',
                        foregroundColor = "#440000",
                        backGroundColor = "#000000"
                    ),
                    stats = StatsTemplate(
                        attack = 2,
                        defense = 1,
                        hp = 10
                    ),
                    behaviors = BehaviorsTemplate(
                        forward = "ForwardMover",
                        backward = "",
                        attack = "SimpleAttacker",
                        defend = ""
                    )
                ),
                Position.create(3,4),
                3
            )
            .withFaction(BlueFaction)
            .build()

        private val defaultEnemyArmy = ArmyBuilder()
            .withHero(
                HeroTemplate(
                    name = "Villain",
                    tile = TileTemplate(
                        char = '@',
                        foregroundColor = "#440033",
                        backGroundColor = "#000000"
                    ),
                    stats = StatsTemplate(
                        attack = 2,
                        defense = 1,
                        hp = 10
                    ),
                    behaviors = BehaviorsTemplate(
                        forward = "ForwardMover",
                        backward = "",
                        attack = "SimpleAttacker",
                        defend = ""
                    )
                ),
                Position.create(8,3),
                1)
            .withFaction(RedFaction)
            .build()

        fun defaultGame() = GameBuilder(
            worldSize = Sizes.create(12, 7))
            .withRedArmy(defaultEnemyArmy)
            .withBlueArmy(defaultPlayerArmy)
            .buildGame()

        fun defaultEditorGame() = GameBuilder(
            worldSize = Sizes.create(12,7))
            .buildGame()

        fun editorGame(army: Army) = GameBuilder(
            worldSize = Sizes.create(12,7))
            .withBlueArmy(army)
            .buildGame()
    }
}