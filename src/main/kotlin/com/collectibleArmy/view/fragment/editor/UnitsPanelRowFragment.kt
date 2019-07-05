package com.collectibleArmy.view.fragment.editor

import com.collectibleArmy.army.templating.Template
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment

class UnitsPanelRowFragment(width: Int, unit: Template): Fragment {
    val button = Components.button()
        .withText(unit.name)
        .build()

    override val root = Components.hbox()
        .withSpacing(1)
        .withSize(width, 1)
        .build().apply {
            addComponent(Components.label()
                .withText(unit.tile.char.toString())
                .build())
            addComponent(button)
        }
}