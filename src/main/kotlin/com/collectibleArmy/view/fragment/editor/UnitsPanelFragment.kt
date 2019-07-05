package com.collectibleArmy.view.fragment.editor

import com.collectibleArmy.GameConfig
import com.collectibleArmy.army.templating.Template
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.component.VBox
import org.hexworks.zircon.api.extensions.processComponentEvents
import org.hexworks.zircon.api.uievent.ComponentEventType
import org.hexworks.zircon.api.uievent.Processed

class UnitsPanelFragment(unitsList: List<Template>,
                         width: Int,
                         private val onSelectUnit: (Template) -> Unit)
    : Fragment {

    override val root = Components.vbox()
        .withSize(width, 43)
        .build().apply {
            val list = this
            addComponent(Components.hbox()
                .withSpacing(1)
                .withSize(width, 1)
                .build().apply {
                    addComponent(Components.label().withText("").withSize(1,1))
                    addComponent(Components.header().withText("Name").withSize(10, 1))
                })
            unitsList.forEach {unit ->
                addRow(width, unit, list)
            }
        }

    private fun addRow(width: Int, unit: Template, list: VBox) {
        list.addFragment(UnitsPanelRowFragment(width, unit).apply {
            button.processComponentEvents(ComponentEventType.ACTIVATED) {
                onSelectUnit(unit)
                Processed
            }
        })
        list.applyColorTheme(GameConfig.THEME)
    }
}