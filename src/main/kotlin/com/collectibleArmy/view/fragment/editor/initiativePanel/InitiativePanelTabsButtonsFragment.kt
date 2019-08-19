package com.collectibleArmy.view.fragment.editor.initiativePanel

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.extensions.box

class InitiativePanelTabsButtonsFragment(width: Int) : Fragment {
    val attackButton = Components.button()
        .withText("Attack")
        .withDecorations()
        .build()

    val defendButton = Components.button()
        .withText("Defend")
        .withDecorations()
        .build()

    val forwardButton = Components.button()
        .withText("Forward")
        .withDecorations()
        .build()

    val retreatButton = Components.button()
        .withText("Retreat")
        .withDecorations()
        .build()

    override val root = Components.vbox()
        .withSpacing(0)
        .withSize(width - 1, 6)
        .withDecorations(box())
        .build().apply {
            addComponent(attackButton)
            addComponent(defendButton)
            addComponent(forwardButton)
            addComponent(retreatButton)
        }
}