package com.collectibleArmy.view.fragment.editor.initiativePanel

import com.collectibleArmy.GameConfig
import com.collectibleArmy.army.HeroHolder
import com.collectibleArmy.army.SoldierHolder
import com.collectibleArmy.army.UnitHolder
import com.collectibleArmy.commands.globals.GlobalCommand
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.component.VBox
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.processComponentEvents
import org.hexworks.zircon.api.uievent.ComponentEventType

class InitiativePanelListFragment(hero: HeroHolder?, soldiers: List<SoldierHolder>,
                                  width: Int,
                                  height: Int,
                                  private var displayedCommand: GlobalCommand,
                                  private val onReorderUnits: () -> Unit,
                                  private val onHighlightUnit: (Position) -> Unit,
                                  private val onStopHighlightingUnit: (Position) -> Unit)
    : Fragment {

    private val fullList: MutableList<UnitHolder> = soldiers.toMutableList<UnitHolder>().also { list ->
        hero?.let {
            list.add(it)
        }
    }.apply {
        sortBy { it.getInitiative(displayedCommand) }
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
                displayedCommand,
                onHighlightUnit,
                onStopHighlightingUnit
            ).apply {
            upButton.processComponentEvents(ComponentEventType.ACTIVATED) {
                if (unit.getInitiative(displayedCommand) > 1) {
                    val targetInitiative = unit.getInitiative(displayedCommand) - 1
                    val unitAtThatInitiative = fullList.firstOrNull { it.getInitiative(displayedCommand) == targetInitiative }
                    unitAtThatInitiative?.incrementInitiative(displayedCommand)
                    unit.decrementInitiative(displayedCommand)
                    onReorderUnits()
                }
            }
            downButton.processComponentEvents(ComponentEventType.ACTIVATED) {
                if (unit.getInitiative(displayedCommand) < fullList.size) {
                    val targetInitiative = unit.getInitiative(displayedCommand) + 1
                    val unitAtThatInitiative = fullList.firstOrNull { it.getInitiative(displayedCommand) == targetInitiative }
                    unitAtThatInitiative?.let {
                        it.decrementInitiative(displayedCommand)
                    }
                    unit.incrementInitiative(displayedCommand)
                    onReorderUnits()
                }
            }
        })
        list.applyColorTheme(GameConfig.THEME)
    }
}