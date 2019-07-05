package com.collectibleArmy.builders

import com.collectibleArmy.attributes.*
import com.collectibleArmy.attributes.flags.BlockOccupier
import com.collectibleArmy.attributes.types.*
import com.collectibleArmy.commands.Attack
import com.collectibleArmy.game.GameContext
import com.collectibleArmy.systems.behaviors.ForwardMover
import com.collectibleArmy.systems.facets.Attackable
import com.collectibleArmy.systems.facets.Destructible
import com.collectibleArmy.systems.facets.Fleeable
import com.collectibleArmy.systems.facets.Movable
import org.hexworks.amethyst.api.Entities
import org.hexworks.amethyst.api.builder.EntityBuilder
import org.hexworks.amethyst.api.entity.EntityType

fun <T : EntityType> newGameEntityOfType(type: T, init: EntityBuilder<T, GameContext>.() -> Unit) =
    Entities.newEntityOfType(type, init)

object EntityFactory {

    fun newBlueOutside() = newGameEntityOfType(Outside) {
        attributes(
            EntityPosition(),
            BlockOccupier,
            EntityTile(GameTileRepository.BLUE_OUTSIDE),
            Faction(BlueFaction)
        )
    }

    fun newRedOutside() = newGameEntityOfType(Outside) {
        attributes(
            EntityPosition(),
            BlockOccupier,
            EntityTile(GameTileRepository.RED_OUTSIDE),
            Faction(RedFaction)
        )
    }

    fun newWall() = newGameEntityOfType(Wall) {
        attributes(
            EntityPosition(),
            BlockOccupier,
            EntityTile(GameTileRepository.WALL),
            Faction(NeutralFaction)
        )
    }

    fun newDummyHero() = newGameEntityOfType(Hero("Hero")) {
        attributes(
            EntityActions(Attack::class),
            CombatStats.create(
                maxHp = 10,
                attackValue = 2,
                defenseValue = 1
            ),
            EntityPosition(),
            BlockOccupier,
            EntityTile(GameTileRepository.HERO),
            Faction(BlueFaction),
            Initiative()
        )
        behaviors(ForwardMover)
        facets(Movable, Attackable, Destructible)
    }

    fun newDummySoldier() = newGameEntityOfType(Soldier("Soldier")) {
        attributes(
            EntityActions(Attack::class),
            CombatStats.create(
                maxHp = 5,
                attackValue = 2,
                defenseValue = 0
            ),
            EntityPosition(),
            BlockOccupier,
            EntityTile(GameTileRepository.SOLDIER),
            Faction(BlueFaction),
            Initiative()
        )
        behaviors(ForwardMover)
        facets(Movable, Fleeable, Attackable, Destructible)
    }

    fun newDummyVillain() = newGameEntityOfType(Hero("Villain")) {
        attributes(
            EntityActions(Attack::class),
            CombatStats.create(
                maxHp = 10,
                attackValue = 2,
                defenseValue = 1
            ),
            EntityPosition(),
            BlockOccupier,
            EntityTile(GameTileRepository.VILLAIN),
            Faction(RedFaction),
            Initiative()
        )
        behaviors(ForwardMover)
        facets(Movable, Attackable, Destructible)
    }

//    fun buildHeroFromTemplate(template: HeroTemplate): GameEntity<Hero> {
//        val attributes = template.attributes.map {
//            attributeToStringMap[it]?.primaryConstructor?.call()
//        }
//        val behaviors = template.behaviors.map {
//            behaviorsToStringMap[it]?.primaryConstructor?.call()
//        }
//        val facets = template.facets.map {
//            facetsToStringMap[it]?.primaryConstructor?.call()
//        }
//
//        return newGameEntityOfType(Hero(template.name)) {
//            attributes
//            behaviors
//            facets
//        }
//    }
}