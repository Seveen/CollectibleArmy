package com.collectibleArmy.builders

import com.collectibleArmy.attributes.*
import com.collectibleArmy.attributes.flags.BlockOccupier
import com.collectibleArmy.attributes.types.*
import com.collectibleArmy.commands.Attack
import com.collectibleArmy.game.GameContext
import com.collectibleArmy.systems.behaviors.BehaviorsToStringMap
import com.collectibleArmy.systems.behaviors.ForwardMover
import com.collectibleArmy.systems.facets.*
import org.hexworks.amethyst.api.Entities
import org.hexworks.amethyst.api.builder.EntityBuilder
import org.hexworks.amethyst.api.entity.EntityType

fun <T : EntityType> newGameEntityOfType(type: T, init: EntityBuilder<T, GameContext>.() -> Unit) =
    Entities.newEntityOfType(type, init)

object EntityFactory {
    private val attributeToStringMap = AttributeToStringMap().map
    private val facetsToStringMap = FacetsToStringMap().map
    private val behaviorsToStringMap = BehaviorsToStringMap().map

    fun newOutside() = newGameEntityOfType(Outside) {
        attributes(
            EntityPosition(),
            BlockOccupier,
            EntityTile(GameTileRepository.OUTSIDE),
            Faction(NeutralFaction)
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
            Faction(PlayerFaction),
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
            Faction(PlayerFaction),
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
            Faction(EnemyFaction),
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