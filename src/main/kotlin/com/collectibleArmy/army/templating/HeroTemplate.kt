package com.collectibleArmy.army.templating

import kotlinx.serialization.Serializable

@Serializable
data class HeroTemplate(override var name: String,
                        override var tile: TileTemplate,
                        override var stats: StatsTemplate,
                        override var behaviors: BehaviorsTemplate
): Template
