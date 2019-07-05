package com.collectibleArmy.builders

import com.collectibleArmy.army.templating.HeroTemplate
import com.collectibleArmy.army.templating.SoldierTemplate
import com.collectibleArmy.attributes.*
import com.collectibleArmy.attributes.flags.BlockOccupier
import com.collectibleArmy.attributes.types.*
import com.collectibleArmy.commands.Attack
import com.collectibleArmy.extensions.GameEntity
import com.collectibleArmy.game.GameContext
import com.collectibleArmy.systems.behaviors.ForwardMover
import com.collectibleArmy.systems.behaviors.SimpleAttacker
import com.collectibleArmy.systems.facets.Attackable
import com.collectibleArmy.systems.facets.Destructible
import com.collectibleArmy.systems.facets.Fleeable
import com.collectibleArmy.systems.facets.Movable
import org.hexworks.amethyst.api.Entities
import org.hexworks.amethyst.api.builder.EntityBuilder
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.amethyst.api.system.Behavior
import org.hexworks.zircon.api.TileColors
import org.hexworks.zircon.api.Tiles

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
        behaviors(ForwardMover, SimpleAttacker)
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
        behaviors(ForwardMover, SimpleAttacker)
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
        behaviors(ForwardMover, SimpleAttacker)
        facets(Movable, Attackable, Destructible)
    }

    //TODO: PAS DE DEEP COPY, LES ATTRIBUTS SONT PARTAGES. GO FAIRE UN VRAI CREATE FROM TEMPLATE NOW
    fun cloneSoldier(entity: GameEntity<Soldier>) = newGameEntityOfType(Soldier(entity.name)) {
        attributes(*entity.attributes.toList().toTypedArray().copyOf())
        behaviors(*entity.behaviors.toList().toTypedArray().copyOf())
        facets(*entity.facets.toList().toTypedArray().copyOf())
    }

    fun cloneHero(entity: GameEntity<Hero>) = newGameEntityOfType(Hero(entity.name)) {
        attributes(*entity.attributes.toList().toTypedArray().copyOf())
        behaviors(*entity.behaviors.toList().toTypedArray().copyOf())
        facets(*entity.facets.toList().toTypedArray().copyOf())
    }

//    fun buildHeroFromTemplate(template: HeroTemplate): GameEntity<Hero> {
//        return newGameEntityOfType(Hero(template.name)) {
//            attributes
//            behaviors
//            facets
//        }
//    }

    fun buildSoldierFromTemplate(template: SoldierTemplate, faction: FactionType): GameEntity<Soldier> {
        val completeBehaviorPath = "com.collectibleArmy.systems.behaviors."

        val tile = Tiles.newBuilder()
            .withCharacter(template.tile.char)
            .withForegroundColor(TileColors.fromString(template.tile.foregroundColor))
            .withBackgroundColor(TileColors.fromString(template.tile.backGroundColor))
            .buildCharacterTile()

        val forwardBehavior = Class.forName(
            completeBehaviorPath + template.behaviors.forward).kotlin

        val attackBehavior = Class.forName(
            completeBehaviorPath + template.behaviors.attack).kotlin

        return newGameEntityOfType(Soldier(template.name)) {
            attributes(
                EntityActions(Attack::class),
                CombatStats.create(
                    maxHp = template.stats.hp,
                    attackValue = template.stats.attack,
                    defenseValue = template.stats.defense
                ),
                EntityPosition(),
                BlockOccupier,
                EntityTile(tile),
                Faction(faction),
                Initiative()
            )
            behaviors(
                forwardBehavior.objectInstance as Behavior<GameContext>,
                attackBehavior.objectInstance as Behavior<GameContext>)
            facets(Movable, Fleeable, Attackable, Destructible)
        }
    }

    fun buildHeroFromTemplate(template: HeroTemplate, faction: FactionType): GameEntity<Hero> {
        val completeBehaviorPath = "com.collectibleArmy.systems.behaviors."

        val tile = Tiles.newBuilder()
            .withCharacter(template.tile.char)
            .withForegroundColor(TileColors.fromString(template.tile.foregroundColor))
            .withBackgroundColor(TileColors.fromString(template.tile.backGroundColor))
            .buildCharacterTile()

        val forwardBehavior = Class.forName(
            completeBehaviorPath + template.behaviors.forward).kotlin

        val attackBehavior = Class.forName(
            completeBehaviorPath + template.behaviors.attack).kotlin

        println(forwardBehavior)

        return newGameEntityOfType(Hero(template.name)) {
            attributes(
                EntityActions(Attack::class),
                CombatStats.create(
                    maxHp = template.stats.hp,
                    attackValue = template.stats.attack,
                    defenseValue = template.stats.defense
                ),
                EntityPosition(),
                BlockOccupier,
                EntityTile(tile),
                Faction(faction),
                Initiative()
            )
            behaviors(
                //TODO: Class should have a single no-arg constructor:?????
                forwardBehavior.objectInstance as Behavior<GameContext>,
                attackBehavior.objectInstance as Behavior<GameContext>)
            facets(Movable, Fleeable, Attackable, Destructible)
        }
    }
}