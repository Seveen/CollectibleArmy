package com.collectibleArmy.game

class Game(val area: Area) {

    companion object {

        fun create(area: Area) = Game(
            area = area
        )
    }
}