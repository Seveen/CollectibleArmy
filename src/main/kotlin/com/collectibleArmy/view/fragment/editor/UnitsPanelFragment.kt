package com.collectibleArmy.view.fragment.editor

import com.collectibleArmy.GameConfig
import com.collectibleArmy.army.templating.HeroTemplate
import com.collectibleArmy.army.templating.SoldierTemplate
import com.collectibleArmy.army.templating.UnitTemplate
import org.hexworks.zircon.api.ComponentDecorations
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.AlignmentStrategy
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.extensions.processComponentEvents
import org.hexworks.zircon.api.uievent.ComponentEventType

class UnitsPanelFragment(private val width: Int,
                         height: Int,
                         alignmentStrategy: AlignmentStrategy,
                         private val soldiersList: List<SoldierTemplate>,
                         private val heroesList: List<HeroTemplate>,
                         private val onSelectUnit: (UnitTemplate) -> Unit
                         ): Fragment {

    private var displayedList = listOf<UnitTemplate>()

    override val root = Components.panel()
        .withSize(width, height)
        .withAlignment(alignmentStrategy)
        .withDecorations(ComponentDecorations.box())
        .build()

    val list = Components.vbox()
        .withSpacing(1)
        .withSize(width - 2, height - 2)
        .build()

    init {
        displayedList = heroesList
        root.addComponent(list)
        rebuild()
    }

    public fun rebuild() {
        list.detachAllComponents()
        list.addFragment(UnitsPanelTabsButtonsFragment(width - 2).apply {
            heroesButton.processComponentEvents(ComponentEventType.ACTIVATED) {
                displayedList = heroesList
                rebuild()
            }
            soldiersButton.processComponentEvents(ComponentEventType.ACTIVATED) {
                displayedList = soldiersList
                rebuild()
            }
        })
        list.addFragment(UnitsPanelListFragment(displayedList, width - 3, onSelectUnit = onSelectUnit))
        root.applyColorTheme(GameConfig.THEME)
    }
}