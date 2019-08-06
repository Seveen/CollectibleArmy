package com.collectibleArmy.army

import com.collectibleArmy.army.templating.HeroTemplate
import com.collectibleArmy.army.templating.SoldierTemplate
import com.collectibleArmy.army.templating.TemplateLoading

object UnitsRepository {
    private val templateLoader: TemplateLoading = TemplateLoading()
    var heroesList: List<HeroTemplate> = templateLoader.loadHeroTemplates()
    var soldiersList: List<SoldierTemplate> = templateLoader.loadSoldierTemplates()

    fun findSoldierTemplateByName(name: String) : SoldierTemplate? {
        return soldiersList.find { it.name == name }
    }

    fun findHeroTemplateByName(name: String) : HeroTemplate? {
        return heroesList.find { it.name == name }
    }
}