package com.collectibleArmy.army

import com.collectibleArmy.functions.logGameEvent
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
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

    fun deleteArmy(name: String) {
        val file = File("$path/$name")
        file.delete()
    }

    fun getArmyList() : List<String> {
        return File(path).list().toList()
    }
}