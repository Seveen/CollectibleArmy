package com.collectibleArmy.army

import com.collectibleArmy.GameConfig.ARENA_WIDTH
import com.collectibleArmy.functions.logGameEvent
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.hexworks.zircon.api.data.Position
import java.io.File

object ArmySaver {
    private val json = Json(JsonConfiguration.Stable)
    private val path = System.getProperty("user.dir") + "/save/armies"

    fun saveArmy(army: Army, name: String) {
        File(path).mkdirs()

        File("$path/$name").writeText(json.stringify(Army.serializer(), army))
    }

    fun loadArmy(name: String) : Army? {
        val file = File("$path/$name")
        return if (file.exists()) {
            json.parse(Army.serializer(), file.readText())
        } else {
            logGameEvent("The army with name: $name was not found at path $path.")
            null
        }
    }

    fun loadEnemyArmy(name: String) : Army? {
        var redArmy: Army? = null
        val file = File("$path/$name")
        val blueArmy: Army? = if (file.exists()) {
            json.parse(Army.serializer(), file.readText())
        } else {
            logGameEvent("The army with name: $name was not found at path $path.")
            null
        }
        blueArmy?.let {
            redArmy = redifyBlueArmy(it)
        }

        return redArmy
    }

    fun deleteArmy(name: String) {
        val file = File("$path/$name")
        file.delete()
    }

    fun getArmyList() : List<String> {
        return File(path).list().toList()
    }

    private fun redifyBlueArmy(blueArmy: Army) : Army {
        val hero = blueArmy.heroHolder
        val soldiers = blueArmy.troopHolders

        hero.initialPosition = Position.create( ARENA_WIDTH - 1 - hero.initialPosition.x, hero.initialPosition.y)
        soldiers.forEach {
            it.initialPosition = Position.create( ARENA_WIDTH - 1 - it.initialPosition.x, it.initialPosition.y)
        }

        return Army(hero, soldiers)
    }
}