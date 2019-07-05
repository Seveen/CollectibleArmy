package com.collectibleArmy.army

import com.collectibleArmy.attributes.types.Hero
import com.collectibleArmy.attributes.types.Soldier
import com.collectibleArmy.extensions.GameEntity
import org.hexworks.zircon.api.data.Position

data class Army(var heroHolder: HeroHolder,
           var troopHolders: List<SoldierHolder>)

data class HeroHolder(var hero: GameEntity<Hero>,
                      var initialPosition: Position,
                      var initialInitiative: Int
)

data class SoldierHolder(var soldier: GameEntity<Soldier>,
                         var initialPosition: Position,
                         var initialInitiative: Int)