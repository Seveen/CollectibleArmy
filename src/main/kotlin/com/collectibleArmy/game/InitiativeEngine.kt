package com.collectibleArmy.game

import com.collectibleArmy.extensions.GameEntity
import com.collectibleArmy.extensions.faction
import com.collectibleArmy.extensions.getInitiative
import com.collectibleArmy.extensions.hasInitiative
import org.hexworks.amethyst.api.Engine
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.Identifier
import org.hexworks.cobalt.logging.api.LoggerFactory

class InitiativeEngine<T : GameContext> : Engine<T> {
    private val logger = LoggerFactory.getLogger(InitiativeEngine::class)

    private val entities = linkedMapOf<Identifier, GameEntity<EntityType>>()

    override fun update(context: T) {
        logger.debug("Updating entities using context: $context.")
        entities.values.toList()
            .filter {
                it.faction == context.command.faction && it.hasInitiative
            }
            .sortedBy {
                logger.debug("Sorting entity: $it, with init:${it.getInitiative(context.command)}.")
                it.getInitiative(context.command)
            }
            .forEach {
                logger.debug("Updating entity: $it, with id:${it.id}.")
                it.update(context)
            }
    }

    override fun addEntity(entity: Entity<EntityType, T>) {
        logger.debug("Adding entity $entity to engine, with id:${entity.id}.")
        entities[entity.id] = (entity as GameEntity<EntityType>)
    }

    override fun removeEntity(entity: Entity<EntityType, T>) {
        logger.debug("Removing entity $entity from engine, with id:${entity.id}.")
        entities.remove(entity.id)
    }

    fun clearEntities() {
        entities.clear()
    }
}