package com.collectibleArmy.view.fragment.editor.armyList

import com.collectibleArmy.GameConfig
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.extensions.box
import org.hexworks.zircon.api.extensions.handleComponentEvents
import org.hexworks.zircon.api.graphics.Symbols
import org.hexworks.zircon.api.uievent.ComponentEventType
import org.hexworks.zircon.api.uievent.Processed

class ArmyListScrollable(private val size: Size, initialList: List<String>, private val onLoad: (String) -> Unit, private val onDelete: (String) -> Unit): Fragment {
    private var verticalOffset = 0

    private val list = initialList.toMutableList()

    private var scrollableBoxSize = size.minus(Size.create(4,4))

    private val scrollableBox = Components.vbox()
        .withSize(scrollableBoxSize)
        .withSpacing(1)
        .build()

    private val upButton = Components.button()
        .withText("${Symbols.TRIANGLE_UP_POINTING_BLACK}")
        .withDecorations()
        .withSize(Size.one())
        .withAlignmentAround(scrollableBox, ComponentAlignment.TOP_RIGHT)
        .build().apply {
            handleComponentEvents(ComponentEventType.ACTIVATED) {
                scrollUpOnce()
                Processed
            }
        }

    private val downButton = Components.button()
        .withText("${Symbols.TRIANGLE_DOWN_POINTING_BLACK}")
        .withDecorations()
        .withSize(Size.one())
        .withAlignmentAround(scrollableBox, ComponentAlignment.BOTTOM_RIGHT)
        .build().apply {
            handleComponentEvents(ComponentEventType.ACTIVATED) {
                scrollDownOnce()
                Processed
            }
        }

    override val root = Components.panel()
        .withSize(size)
        .withDecorations(box())
        .build().apply {
            addComponent(scrollableBox)
            addComponent(upButton)
            addComponent(downButton)
            rebuildList()
        }

    private fun onArmyDeleted(name: String) {
        onDelete(name)
        println(list)
        list.remove(name)
        println(list)
        rebuildList()
    }

    private fun scrollUpOnce() {
        if (verticalOffset > 0) {
            verticalOffset--
        }
        rebuildList()
    }

    private fun scrollDownOnce() {
        if (verticalOffset < list.size) {
            verticalOffset++
        }
        rebuildList()
    }

    private fun rebuildList() {
        scrollableBox.detachAllComponents()
        for (idx in 0 until size.height) {
            val realIdx = idx + verticalOffset
            if (realIdx < list.size) {
                scrollableBox.addFragment(
                    ArmyListRowFragment(scrollableBoxSize.minus(Size.one()).width ,list[realIdx]).apply {
                        loadButton.handleComponentEvents(ComponentEventType.ACTIVATED) {
                            onLoad(list[realIdx])
                            Processed
                        }
                        deleteButton.handleComponentEvents(ComponentEventType.ACTIVATED) {
                            onArmyDeleted(list[realIdx])
                            Processed
                        }
                    }
                )
            }
        }
        scrollableBox.applyColorTheme(GameConfig.THEME)
    }
}