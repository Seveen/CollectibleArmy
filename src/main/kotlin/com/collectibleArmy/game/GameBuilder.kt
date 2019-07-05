package com.collectibleArmy.game

import com.collectibleArmy.army.Army
import com.collectibleArmy.army.ArmyBuilder
import com.collectibleArmy.builders.EntityFactory
import com.collectibleArmy.extensions.initiative
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size

class GameBuilder(val worldSize: Size) {

    private val visibleSize = worldSize

    private lateinit var playerArmy: Army
    private lateinit var enemyArmy: Army

    private val area = AreaBuilder(worldSize)
        .makeArena()
        .build(visibleSize = visibleSize)

    fun withPlayerArmy(army: Army): GameBuilder {
        playerArmy = army
        return this
    }

    fun withEnemyArmy(army: Army): GameBuilder {
        enemyArmy = army
        return this
    }

    fun buildGame(): Game {
        prepareWorld()

        loadArmies(playerArmy, enemyArmy)

        return Game.create(
            playerArmy = playerArmy,
            enemyArmy = enemyArmy,
            area = area
        )
    }

    private fun prepareWorld() = also {
        area.scrollUpBy(area.actualSize().zLength)
    }

    private fun loadArmies(playerArmy: Army, enemyArmy: Army) {
        playerArmy.heroHolder.hero.initiative = playerArmy.heroHolder.initialInitiative
        area.addEntity(playerArmy.heroHolder.hero, playerArmy.heroHolder.initialPosition)

        enemyArmy.heroHolder.hero.initiative = enemyArmy.heroHolder.initialInitiative
        area.addEntity(enemyArmy.heroHolder.hero, enemyArmy.heroHolder.initialPosition)

        playerArmy.troopHolders.forEach {
            it.soldier.initiative = it.initialInitiative
            area.addEntity(it.soldier, it.initialPosition)
        }

        enemyArmy.troopHolders.forEach {
            area.addEntity(it.soldier, it.initialPosition)
        }
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
                EntityFactory.newDummyHero(),
                Position.create(3,3),
                1)
            .withSoldier(
                EntityFactory.newDummySoldier(),
                Position.create(3,2),
                2
            )
            .withSoldier(
                EntityFactory.newDummySoldier(),
                Position.create(3,4),
                3
            )
            .build()

        private val defaultEnemyArmy = ArmyBuilder()
            .withHero(
                EntityFactory.newDummyVillain(),
                Position.create(8,3),
                1)
            .build()

        fun defaultGame() = GameBuilder(
            worldSize = Sizes.create(12, 7))
            .withEnemyArmy(defaultEnemyArmy)
            .withPlayerArmy(defaultPlayerArmy)
            .buildGame()
    }
}