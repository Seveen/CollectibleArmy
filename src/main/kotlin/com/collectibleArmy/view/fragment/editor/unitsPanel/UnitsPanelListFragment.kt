package com.collectibleArmy.view.fragment.editor.unitsPanel

import com.collectibleArmy.GameConfig
import com.collectibleArmy.army.templating.UnitTemplate
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.component.VBox
import org.hexworks.zircon.api.extensions.processComponentEvents
import org.hexworks.zircon.api.uievent.ComponentEventType

class UnitsPanelListFragment(unitsList: List<UnitTemplate>,
                             width: Int,
                             private val onSelectUnit: (UnitTemplate) -> Unit)
    : Fragment {

    override val root = Components.vbox()
        .withSize(width, 33)
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

    private fun addRow(width: Int, unit: UnitTemplate, list: VBox) {
        list.addFragment(UnitsPanelRowFragment(width, unit).apply {
            button.processComponentEvents(ComponentEventType.ACTIVATED) {
                onSelectUnit(unit)
            }
        })
        list.applyColorTheme(GameConfig.THEME)
    }
}