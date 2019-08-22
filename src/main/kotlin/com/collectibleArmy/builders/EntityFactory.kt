package com.collectibleArmy.builders

import com.collectibleArmy.army.templating.HeroTemplate
import com.collectibleArmy.army.templating.SoldierTemplate
import com.collectibleArmy.attributes.*
import com.collectibleArmy.attributes.flags.BlockOccupier
import com.collectibleArmy.attributes.types.*
import com.collectibleArmy.commands.Attack
import com.collectibleArmy.commands.globals.GlobalAttack
import com.collectibleArmy.commands.globals.GlobalDefend
import com.collectibleArmy.commands.globals.GlobalForward
import com.collectibleArmy.commands.globals.GlobalRetreat
import com.collectibleArmy.extensions.GameEntity
import com.collectibleArmy.game.GameContext
import com.collectibleArmy.systems.behaviors.GlobalBehavior
import com.collectibleArmy.systems.facets.Attackable
import com.collectibleArmy.systems.facets.Destructible
import com.collectibleArmy.systems.facets.Fleeable
import com.collectibleArmy.systems.facets.Movable
import org.hexworks.amethyst.api.Entities
import org.hexworks.amethyst.api.builder.EntityBuilder
import org.hexworks.amethyst.api.entity.EntityType
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

    fun buildSoldierFromTemplate(template: SoldierTemplate, faction: FactionType): GameEntity<Soldier> {
        val completeBehaviorPath = "com.collectibleArmy.systems.behaviors."

        val tile = Tiles.newBuilder()
            .withCharacter(template.tile.char)
            .withForegroundColor(TileColors.fromString(template.tile.foregroundColor))
            .withBackgroundColor(TileColors.fromString(template.tile.backgroundColor))
            .buildCharacterTile()

        val forwardBehavior = Class.forName(
            completeBehaviorPath + template.behaviors.forward).kotlin.objectInstance as GlobalBehavior
        forwardBehavior.setResponseCommand(GlobalForward(faction))

        val retreatBehavior = Class.forName(
            completeBehaviorPath + template.behaviors.retreat).kotlin.objectInstance as GlobalBehavior
        retreatBehavior.setResponseCommand(GlobalRetreat(faction))

        val attackBehavior = Class.forName(
            completeBehaviorPath + template.behaviors.attack).kotlin.objectInstance as GlobalBehavior
        attackBehavior.setResponseCommand(GlobalAttack(faction))

        val defenseBehavior = Class.forName(
            completeBehaviorPath + template.behaviors.defend).kotlin.objectInstance as GlobalBehavior
        defenseBehavior.setResponseCommand(GlobalDefend(faction))

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
                forwardBehavior,
                retreatBehavior,
                attackBehavior,
                defenseBehavior
            )
            facets(
                Movable,
                Fleeable,
                Attackable,
                Destructible
            )
        }
    }

    fun buildHeroFromTemplate(template: HeroTemplate, faction: FactionType): GameEntity<Hero> {
        val completeBehaviorPath = "com.collectibleArmy.systems.behaviors."

        val tile = Tiles.newBuilder()
            .withCharacter(template.tile.char)
            .withForegroundColor(TileColors.fromString(template.tile.foregroundColor))
            .withBackgroundColor(TileColors.fromString(template.tile.backgroundColor))
            .buildCharacterTile()

        val forwardBehavior = Class.forName(
            completeBehaviorPath + template.behaviors.forward).kotlin.objectInstance as GlobalBehavior
        forwardBehavior.setResponseCommand(GlobalForward(faction))

        val retreatBehavior = Class.forName(
            completeBehaviorPath + template.behaviors.retreat).kotlin.objectInstance as GlobalBehavior
        retreatBehavior.setResponseCommand(GlobalRetreat(faction))

        val attackBehavior = Class.forName(
            completeBehaviorPath + template.behaviors.attack).kotlin.objectInstance as GlobalBehavior
        attackBehavior.setResponseCommand(GlobalAttack(faction))

        val defenseBehavior = Class.forName(
            completeBehaviorPath + template.behaviors.defend).kotlin.objectInstance as GlobalBehavior
        defenseBehavior.setResponseCommand(GlobalDefend(faction))

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
                forwardBehavior,
                retreatBehavior,
                attackBehavior,
                defenseBehavior
            )
            facets(
                Movable,
                Fleeable,
                Attackable,
                Destructible
            )
        }
    }
}