package com.collectibleArmy.army.templating

interface Template{
    var name: String
    var tile: TileTemplate
    var stats: StatsTemplate
    var behaviors: BehaviorsTemplate
}