package com.collectibleArmy.extensions

import com.collectibleArmy.attributes.*
import com.collectibleArmy.attributes.flags.BlockOccupier
import com.collectibleArmy.attributes.types.Combatant
import com.collectibleArmy.attributes.types.FactionType
import com.collectibleArmy.attributes.types.combatStats
import com.collectibleArmy.commands.globals.*
import com.collectibleArmy.game.GameContext
import org.hexworks.amethyst.api.Attribute
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.cobalt.datatypes.extensions.orElseThrow
import org.hexworks.zircon.api.data.Tile
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.isSuperclassOf

var AnyGameEntity.position
    get() = tryToFindAttribute(EntityPosition::class).position
    set(value) {
        findAttribute(EntityPosition::class).map {
            it.position = value
        }
    }

val AnyGameEntity.tile: Tile
    get() = this.tryToFindAttribute(EntityTile::class).tile

fun AnyGameEntity.getInitiative(command: GlobalCommand): Int {
    val initiative = this.tryToFindAttribute(Initiative::class)
    return when(command::class) {
        GlobalAttack::class -> initiative.attackInitiative
        GlobalDefend::class -> initiative.defendInitiative
        GlobalForward::class -> initiative.forwardInitiative
        GlobalRetreat::class -> initiative.retreatInitiative
        GlobalCast::class -> initiative.castInitiative
        else -> 0
    }
}

fun AnyGameEntity.setInitiative(command: GlobalCommand, value: Int) {
    val initiative = this.tryToFindAttribute(Initiative::class)
    when(command::class) {
        GlobalAttack::class -> initiative.attackInitiative = value
        GlobalDefend::class -> initiative.defendInitiative = value
        GlobalForward::class -> initiative.forwardInitiative = value
        GlobalRetreat::class -> initiative.retreatInitiative = value
        GlobalCast::class -> initiative.castInitiative = value
        else -> {}
    }
}

val AnyGameEntity.occupiesBlock: Boolean
    get() = findAttribute(BlockOccupier::class).isPresent

val AnyGameEntity.hasFaction: Boolean
    get() = this.findAttribute(Faction::class).isPresent

val AnyGameEntity.faction: FactionType
    get() = this.tryToFindAttribute(Faction::class).faction

val AnyGameEntity.hasInitiative: Boolean
    get() = this.findAttribute(Initiative::class).isPresent

fun GameEntity<Combatant>.whenHasNoHealthLeft(fn: () -> Unit) {
    if (combatStats.hp <= 0) {
        fn()
    }
}

val AnyGameEntity.attackValue: Int
    get() {
        val combat = findAttribute(CombatStats::class).map { it.attackValue }.orElse(0)

        return combat
    }

val AnyGameEntity.defenseValue: Int
    get() {
        val combat = findAttribute(CombatStats::class).map { it.defenseValue }.orElse(0)

        return combat
    }

fun AnyGameEntity.tryActionsOn(context: GameContext, target: AnyGameEntity): Response {
    var result: Response = Pass
    findAttribute(EntityActions::class).map {
        it.createActionsFor(context, this, target).forEach { action ->
            if (target.executeCommand(action) is Consumed) {
                result = Consumed
                return@forEach
            }
        }
    }
    return result
}

fun <T : Attribute> AnyGameEntity.tryToFindAttribute(klass: KClass<T>): T = findAttribute(klass).orElseThrow {
    NoSuchElementException("Entity '$this' has no property with type '${klass.simpleName}'.")
}

inline fun <reified T : EntityType> Iterable<AnyGameEntity>.filterType(): List<Entity<T, GameContext>> {
    return filter { T::class.isSuperclassOf(it.type::class)}.toList() as List<Entity<T, GameContext>>
}

inline fun <reified T : EntityType> AnyGameEntity.whenTypeIs(fn: (Entity<T, GameContext>) -> Unit) {
    if (this.type::class.isSubclassOf(T::class)) {
        fn(this as Entity<T, GameContext>)
    }
}
