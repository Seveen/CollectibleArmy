package com.collectibleArmy.army

import com.collectibleArmy.army.templating.HeroTemplate
import com.collectibleArmy.army.templating.SoldierTemplate
import org.hexworks.zircon.api.data.Position

class ArmyBuilder {
    private lateinit var hero: HeroHolder
    private var soldierList: MutableList<SoldierHolder> = mutableListOf()

    fun withHero(entity: HeroTemplate,
                 initialPosition: Position,
                 initialInitiative: Int): ArmyBuilder {
        hero = HeroHolder(entity, initialPosition, initialInitiative)
        return this
    }

    fun withSoldier(entity: SoldierTemplate,
                    initialPosition: Position,
                    initialInitiative: Int): ArmyBuilder {
        soldierList.add(SoldierHolder(entity, initialPosition, initialInitiative))
        return this
    }

    fun build(): Army {
        return Army(hero, soldierList)
    }
}

