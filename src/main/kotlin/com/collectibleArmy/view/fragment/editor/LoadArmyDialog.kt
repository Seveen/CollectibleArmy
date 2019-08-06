package com.collectibleArmy.view.fragment.editor

import com.collectibleArmy.view.fragment.Dialog
import com.collectibleArmy.view.fragment.editor.armyList.ArmyListScrollable
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.extensions.box
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.internal.component.modal.EmptyModalResult

class LoadArmyDialog(screen: Screen, private val list: List<String>, val onLoad: (String) -> Unit, onDelete: (String) -> Unit)
    : Dialog<EmptyModalResult>(screen, true, EmptyModalResult) {

    private val size = Sizes.create(45,45)

    private val armyListScrollable = ArmyListScrollable(
        size.minus(Size.create(2, 3)),
        list,
        ::onArmyLoaded,
        onDelete
    )

    override val container = Components.panel()
        .withDecorations(box(title = "Load Army"))
        .withSize(size)
        .build().apply {
            addFragment(
                armyListScrollable
            )
        }

    private fun onArmyLoaded(name: String) {
        onLoad(name)
        root.close(EmptyModalResult)
    }
}