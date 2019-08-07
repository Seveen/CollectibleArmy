package com.collectibleArmy.view.fragment.editor.initiativePanel

import com.collectibleArmy.GameConfig
import com.collectibleArmy.army.HeroHolder
import com.collectibleArmy.army.SoldierHolder
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.AlignmentStrategy
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.box

class InitiativePanelFragment(private val width: Int,
                              private val height: Int,
                              alignmentStrategy: AlignmentStrategy,
                              private val onHighlightUnit: (Position) -> Unit,
                              private val onStopHighlightingUnit: (Position) -> Unit,
                              private val onReorder: () -> Unit,
                              private var hero: HeroHolder?,
                              private var soldiers: List<SoldierHolder>): Fragment {

    override val root = Components.panel()
        .withSize(width, height)
        .withAlignment(alignmentStrategy)
        .withDecorations(box())
        .build()

    private val label = Components.label()
        .withText("Initiative")
        .withSize(width - 2, 1)
        .withDecorations()
        .withAlignmentWithin(root, ComponentAlignment.TOP_CENTER)
        .build()

    val list = Components.vbox()
        .withSpacing(1)
        .withDecorations()
        .withSize(width - 2, height - 2)
        .build()

    init {
        root.addComponent(list)
        rebuild()
    }

    private fun rebuild() {
        list.detachAllComponents()
        list.addComponent(label)
        list.addFragment(
            InitiativePanelListFragment(
                hero, soldiers, width - 3, height - 4,
                ::onReorderRebuild, onHighlightUnit, onStopHighlightingUnit
            )
        )
        list.applyColorTheme(GameConfig.THEME)
    }

    private fun onReorderRebuild() {
        onReorder()
        rebuild()
    }

    fun rebuildWith(hero: HeroHolder?, soldiers: List<SoldierHolder>) {
        this.hero = hero
        this.soldiers = soldiers
        rebuild()
    }
}