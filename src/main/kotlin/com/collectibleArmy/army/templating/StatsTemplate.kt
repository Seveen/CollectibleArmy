package com.collectibleArmy.army.templating

import kotlinx.serialization.Serializable

@Serializable
data class StatsTemplate(val attack: Int, val defense: Int, val hp: Int)