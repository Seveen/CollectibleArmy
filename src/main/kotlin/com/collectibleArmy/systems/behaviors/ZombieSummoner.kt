package com.collectibleArmy.systems.behaviors

import com.collectibleArmy.army.UnitsRepository.findSoldierTemplateByName
import com.collectibleArmy.builders.EntityFactory.buildSoldierFromTemplate
import com.collectibleArmy.extensions.GameEntity
import com.collectibleArmy.extensions.faction
import com.collectibleArmy.extensions.forward
import com.collectibleArmy.extensions.position
import com.collectibleArmy.game.GameContext
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.logging.api.LoggerFactory

object ZombieSummoner : GlobalBehavior() {
    private val logger = LoggerFactory.getLogger(this::class)

    override fun action(entity: GameEntity<EntityType>, context: GameContext) {
        val (area, _, _) = context
        val zombieTemplate = findSoldierTemplateByName("Zombie")
        zombieTemplate?.let {
            val zombie = buildSoldierFromTemplate(it, entity.faction)
            val frontPosition = entity.position.forward(entity.faction)
            if (area.isPositionEmpty(frontPosition)) {
                area.addEntity(zombie, entity.position.forward(entity.faction))
            }
        }
    }
}