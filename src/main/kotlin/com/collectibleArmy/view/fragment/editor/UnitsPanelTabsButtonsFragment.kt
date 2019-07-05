package com.collectibleArmy.view.fragment.editor

import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment

class UnitsPanelTabsButtonsFragment(width: Int) : Fragment {
    val soldiersButton = Components.button()
        .withText("Soldiers")
        .withDecorations(box())
        .build()

    val heroesButton = Components.button()
        .withText("Heroes")
        .withDecorations(box())
        .build()

    override val root = Components.hbox()
        .withSpacing(1)
        .withSize(width - 1, 4)
        .build().apply {
            addComponent(heroesButton)
            addComponent(soldiersButton)
        }
}