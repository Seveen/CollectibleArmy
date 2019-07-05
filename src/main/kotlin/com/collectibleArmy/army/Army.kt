package com.collectibleArmy.army

import com.collectibleArmy.army.templating.HeroTemplate
import com.collectibleArmy.army.templating.SoldierTemplate
import com.collectibleArmy.attributes.types.FactionType
import org.hexworks.zircon.api.data.Position

data class Army(var heroHolder: HeroHolder,
                var troopHolders: List<SoldierHolder>,
                var faction: FactionType)

data class HeroHolder(var hero: HeroTemplate,
                      var initialPosition: Position,
                      var initialInitiative: Int
)

data class SoldierHolder(var soldier: SoldierTemplate,
                         var initialPosition: Position,
                         var initialInitiative: Int)