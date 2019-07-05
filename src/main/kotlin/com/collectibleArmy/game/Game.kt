package com.collectibleArmy.game

import com.collectibleArmy.army.Army

class Game(val area: Area,
           val playerArmy: Army,
           val enemyArmy: Army
           ) {

    companion object {

        fun create(playerArmy: Army,
                   enemyArmy: Army,
                   area: Area) = Game(
            area = area,
            playerArmy = playerArmy,
            enemyArmy = enemyArmy
        )
    }
}