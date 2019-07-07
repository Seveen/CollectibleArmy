package com.collectibleArmy.army.templating

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import java.io.File

class TemplateLoading {
    val json = Json(JsonConfiguration.Stable)

    fun loadSoldierTemplates(): List<SoldierTemplate> {
        val jsonData = File("src/main/resources/templates/soldiers.json").readText()
        return json.parse(SoldierTemplate.serializer().list, jsonData)
    }

    fun loadHeroTemplates(): List<HeroTemplate> {
        val jsonData = File("src/main/resources/templates/heroes.json").readText()
        return json.parse(HeroTemplate.serializer().list, jsonData)
    }
}
