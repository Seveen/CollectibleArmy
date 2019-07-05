package com.collectibleArmy.builders

import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.graphics.Symbols


object GameTileRepository {
    val EMPTY: CharacterTile = Tiles.empty()

    val FLOOR: CharacterTile = Tiles.newBuilder()
        .withCharacter(Symbols.INTERPUNCT)
        .withForegroundColor(GameColors.FLOOR_FOREGROUND)
        .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
        .buildCharacterTile()

    val WALL: CharacterTile = Tiles.newBuilder()
        .withCharacter('#')
        .withForegroundColor(GameColors.WALL_FOREGROUND)
        .withBackgroundColor(GameColors.WALL_BACKGROUND)
        .buildCharacterTile()

    val RED_OUTSIDE: CharacterTile = Tiles.newBuilder()
        .withCharacter(Symbols.INTERPUNCT)
        .withForegroundColor(GameColors.FLOOR_FOREGROUND)
        .withBackgroundColor(GameColors.RED_OUTSIDE_BACKGROUND)
        .buildCharacterTile()

    val BLUE_OUTSIDE: CharacterTile = Tiles.newBuilder()
        .withCharacter(Symbols.INTERPUNCT)
        .withForegroundColor(GameColors.FLOOR_FOREGROUND)
        .withBackgroundColor(GameColors.BLUE_OUTSIDE_BACKGROUND)
        .buildCharacterTile()

    val HERO: CharacterTile = Tiles.newBuilder()
        .withCharacter('@')
        .withForegroundColor(GameColors.ACCENT_COLOR)
        .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
        .buildCharacterTile()

    val SOLDIER: CharacterTile = Tiles.newBuilder()
        .withCharacter('S')
        .withForegroundColor(GameColors.ACCENT_COLOR)
        .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
        .buildCharacterTile()

    val VILLAIN: CharacterTile = Tiles.newBuilder()
        .withCharacter('@')
        .withForegroundColor(GameColors.VILLAIN_COLOR)
        .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
        .buildCharacterTile()
}