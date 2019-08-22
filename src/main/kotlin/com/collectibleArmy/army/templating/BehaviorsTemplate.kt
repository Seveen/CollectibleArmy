package com.collectibleArmy.army.templating

import kotlinx.serialization.Serializable

@Serializable
data class BehaviorsTemplate(val forward: String, val retreat: String, val attack: String, val defend: String)