package com.collectibleArmy.game

import com.collectibleArmy.extensions.GameEntity
import com.collectibleArmy.extensions.faction
import com.collectibleArmy.extensions.initiative
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
                it.faction == context.command.faction
            }
            .sortedBy {
                logger.debug("Sorting entity: ${it.initiative}.")
                it.initiative
            }
            .forEach {
                logger.debug("Updating entity: $it.")
                it.update(context)
            }
    }

    override fun addEntity(entity: Entity<EntityType, T>) {
        logger.debug("Adding entity $entity to engine.")
        entities[entity.id] = (entity as GameEntity<EntityType>)
    }

    override fun removeEntity(entity: Entity<EntityType, T>) {
        logger.debug("Removing entity $entity from engine.")
        entities.remove(entity.id)
    }
}