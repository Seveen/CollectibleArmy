package com.collectibleArmy.view.fragment.editor.initiativePanel

import com.collectibleArmy.GameConfig
import com.collectibleArmy.army.HeroHolder
import com.collectibleArmy.army.SoldierHolder
import com.collectibleArmy.army.UnitHolder
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.builder.component.ComponentStyleSetBuilder
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.processMouseEvents
import org.hexworks.zircon.api.graphics.StyleSet
import org.hexworks.zircon.api.graphics.Symbols
import org.hexworks.zircon.api.uievent.MouseEventType

class InitiativePanelRowFragment(width: Int, unit: UnitHolder, onHighlightUnit: (Position) -> Unit, onStopHighlightingUnit: (Position) -> Unit): Fragment {
    private val initiativeLabelWidth = 3
    private val arrowsWidth = 2
    private val totalHeight = 1

    private val newStyleSet = ComponentStyleSetBuilder.newBuilder()
        .withDefaultStyle(StyleSet.defaultStyle())
        .withMouseOverStyle(StyleSet.defaultStyle().withBackgroundColor(GameConfig.THEME.accentColor))
        .build()

    val upButton = Components.button()
        .withText("${Symbols.ARROW_UP}")
        .withDecorations()
        .build()

    val downButton = Components.button()
        .withText("${Symbols.ARROW_DOWN}")
        .withDecorations()
        .build()

    private val unitName = when {
        (unit is HeroHolder) -> unit.hero.name
        (unit is SoldierHolder) -> unit.soldier.name
        else -> ""
    }

    override val root = Components.hbox()
        .withSpacing(0)
        .withSize(width, totalHeight)

        .build().apply {
            addComponent(
                Components.label()
                    .withText("${unit.initialInitiative}")
                    .withSize(initiativeLabelWidth, totalHeight)
                    .withDecorations()
                    .build()
            )
            addComponent(
                Components.button()
                    .withSize(width - (initiativeLabelWidth + arrowsWidth), totalHeight)
                    .withText(unitName)
                    .withDecorations()
                    .build().apply {
                        processMouseEvents(MouseEventType.MOUSE_ENTERED) { _, _ ->
                            onHighlightUnit(unit.initialPosition)
                        }
                        processMouseEvents(MouseEventType.MOUSE_EXITED) { _, _ ->
                            onStopHighlightingUnit(unit.initialPosition)
                        }
                    }
            )
            addComponent(upButton)
            addComponent(downButton)
        }
}