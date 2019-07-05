package com.collectibleArmy.army

import com.collectibleArmy.attributes.types.Hero
import com.collectibleArmy.attributes.types.Soldier
import com.collectibleArmy.extensions.GameEntity
import org.hexworks.zircon.api.data.Position

class ArmyBuilder {
    private lateinit var hero: HeroHolder
    private var soldierList: MutableList<SoldierHolder> = mutableListOf()

    fun withHero(entity: GameEntity<Hero>,
                 initialPosition: Position,
                 initialInitiative: Int): ArmyBuilder {
        hero = HeroHolder(entity, initialPosition, initialInitiative)
        return this
    }

    fun withSoldier(entity: GameEntity<Soldier>,
                    initialPosition: Position,
                    initialInitiative: Int): ArmyBuilder {
        soldierList.add(SoldierHolder(entity, initialPosition, initialInitiative))
        return this
    }

    fun build(): Army {
        return Army(hero, soldierList)
    }
}

