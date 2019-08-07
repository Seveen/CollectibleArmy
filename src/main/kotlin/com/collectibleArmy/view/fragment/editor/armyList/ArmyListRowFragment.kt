package com.collectibleArmy.view.fragment.editor.armyList

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.graphics.Symbols

class ArmyListRowFragment(width: Int, text: String, withDeleteButton: Boolean): Fragment {

    private val labelSize = Sizes.create(width - 6, 1)

    private val label = Components.label().withSize(labelSize).withText(text).build()
    val loadButton = Components.button().withText("${Symbols.ARROW_UP}").build()
    val deleteButton = Components.button().withText("X").build()

    override val root = Components.hbox().withSize(width, 1).build().apply {
        addComponent(label)
        addComponent(loadButton)
        if (withDeleteButton) {
            addComponent(deleteButton)
        }
    }
}