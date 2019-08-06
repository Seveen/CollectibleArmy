package com.collectibleArmy.army.templating

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list

class TemplateLoading {
    private val json = Json(JsonConfiguration.Stable)

    fun loadSoldierTemplates(): List<SoldierTemplate> {
        val jsonData = javaClass.getResource("/templates/soldiers.json").readText()
        return json.parse(SoldierTemplate.serializer().list, jsonData)
    }

    fun loadHeroTemplates(): List<HeroTemplate> {
        val jsonData = javaClass.getResource("/templates/heroes.json").readText()
        return json.parse(HeroTemplate.serializer().list, jsonData)
    }
}
