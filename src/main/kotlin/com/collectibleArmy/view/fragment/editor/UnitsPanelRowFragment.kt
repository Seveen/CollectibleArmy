package com.collectibleArmy.view.fragment.editor

import com.collectibleArmy.army.templating.UnitTemplate
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.Fragment

class UnitsPanelRowFragment(width: Int, unit: UnitTemplate): Fragment {
    val button = Components.button()
        .withText(unit.name)
        .build()

    //TODO: Investigate icon error
    private val iconTile = Tiles.newBuilder()
        .withCharacter(unit.tile.char)
        .withForegroundColor(TileColor.fromString(unit.tile.foregroundColor))
        .withBackgroundColor(TileColor.fromString(unit.tile.backgroundColor))
        .buildGraphicTile()

    override val root = Components.hbox()
        .withSpacing(1)
        .withSize(width, 1)
        .build().apply {
            addComponent(Components.label()
                .withText("${unit.tile.char}")
                .build())
            addComponent(button)
        }
}