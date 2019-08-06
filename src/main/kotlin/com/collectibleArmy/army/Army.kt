package com.collectibleArmy.army

import kotlinx.serialization.Serializable

@Serializable
data class Army(var heroHolder: HeroHolder,
                var troopHolders: List<SoldierHolder>)
