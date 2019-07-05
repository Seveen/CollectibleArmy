package com.collectibleArmy.army

import com.collectibleArmy.army.templating.HeroTemplate
import com.collectibleArmy.army.templating.SoldierTemplate
import com.collectibleArmy.attributes.types.FactionType
import com.collectibleArmy.attributes.types.NeutralFaction
import org.hexworks.zircon.api.data.Position

class ArmyBuilder {
    private lateinit var hero: HeroHolder
    private var soldierList: MutableList<SoldierHolder> = mutableListOf()
    private var faction: FactionType = NeutralFaction

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

    fun withFaction(fact: FactionType): ArmyBuilder {
        faction = fact
        return this
    }

    fun build(): Army {
        return Army(hero, soldierList, faction)
    }
}

