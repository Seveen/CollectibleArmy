package com.collectibleArmy.army

import kotlinx.serialization.Serializable

@Serializable
data class HeroTemplate(
    val name: String,
    val attributes: List<String>,
    val behaviors: List<String>,
    val facets: List<String>
)
