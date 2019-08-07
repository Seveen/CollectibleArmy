package com.collectibleArmy.view.fragment.editor.initiativePanel

import com.collectibleArmy.GameConfig
import com.collectibleArmy.army.HeroHolder
import com.collectibleArmy.army.SoldierHolder
import com.collectibleArmy.army.UnitHolder
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.component.VBox
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.processComponentEvents
import org.hexworks.zircon.api.uievent.ComponentEventType

class InitiativePanelListFragment(hero: HeroHolder?, soldiers: List<SoldierHolder>,
                                  width: Int,
                                  height: Int,
                                  private val onReorderUnits: () -> Unit,
                                  private val onHighlightUnit: (Position) -> Unit,
                                  private val onStopHighlightingUnit: (Position) -> Unit)
    : Fragment {

    private val fullList: MutableList<UnitHolder> = soldiers.toMutableList<UnitHolder>().also { list ->
        hero?.let {
            list.add(it)
        }
    }.apply {
        sortBy { it.initialInitiative }
    }

    override val root = Components.vbox()
        .withSize(width, height)
        .build().apply {
            val list = this
            fullList.forEach {unit ->
                addRow(width, unit, list)
            }
        }

    private fun addRow(width: Int, unit: UnitHolder, list: VBox) {
        list.addFragment(
            InitiativePanelRowFragment(
                width,
                unit,
                onHighlightUnit,
                onStopHighlightingUnit
            ).apply {
            upButton.processComponentEvents(ComponentEventType.ACTIVATED) {
                if (unit.initialInitiative > 1) {
                    val targetInitiative = unit.initialInitiative - 1
                    val unitAtThatInitiative = fullList.firstOrNull { it.initialInitiative == targetInitiative }
                    unitAtThatInitiative?.let {
                        it.initialInitiative++
                    }
                    unit.initialInitiative--
                    onReorderUnits()
                }
            }
            downButton.processComponentEvents(ComponentEventType.ACTIVATED) {
                if (unit.initialInitiative < fullList.size) {
                    val targetInitiative = unit.initialInitiative + 1
                    val unitAtThatInitiative = fullList.firstOrNull { it.initialInitiative == targetInitiative }
                    unitAtThatInitiative?.let {
                        it.initialInitiative--
                    }
                    unit.initialInitiative++
                    onReorderUnits()
                }
            }
        })
        list.applyColorTheme(GameConfig.THEME)
    }
}