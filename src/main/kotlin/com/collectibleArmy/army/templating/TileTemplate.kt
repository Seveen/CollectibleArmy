package com.collectibleArmy.army.templating

import kotlinx.serialization.Serializable

@Serializable
data class TileTemplate(val char: Char, val backgroundColor: String, val foregroundColor: String)