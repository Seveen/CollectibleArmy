package com.collectibleArmy.army.templating

interface UnitTemplate {
    var name: String
    var tile: TileTemplate
    var stats: StatsTemplate
    var behaviors: BehaviorsTemplate
}