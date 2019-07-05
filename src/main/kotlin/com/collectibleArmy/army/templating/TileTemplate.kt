package com.collectibleArmy.army.templating

import kotlinx.serialization.Serializable

@Serializable
data class TileTemplate(val char: Char, val backGroundColor: String, val foregroundColor: String)