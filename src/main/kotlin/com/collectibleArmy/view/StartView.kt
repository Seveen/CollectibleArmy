package com.collectibleArmy.view

import com.collectibleArmy.GameConfig
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.extensions.processComponentEvents
import org.hexworks.zircon.api.mvc.base.BaseView
import org.hexworks.zircon.api.uievent.ComponentEventType
import org.hexworks.zircon.api.uievent.Processed

class StartView : BaseView() {
    override val theme = GameConfig.THEME

    override fun onDock() {
        val msg = "Collectible Army"
        val header = Components.textBox(msg.length)
            .addHeader(msg)
            .addNewLine()
            .withAlignmentWithin(screen, ComponentAlignment.CENTER)
            .build()

        val newGameButton = Components.button()
            .withText("New Game")
            .withDecorations()
            .build()

        newGameButton.processComponentEvents(ComponentEventType.ACTIVATED) {
            replaceWith(SelectMissionView())
            close()
            Processed
        }

        val editorButton = Components.button()
            .withText("Editor")
            .withDecorations()
            .build()

        editorButton.processComponentEvents(ComponentEventType.ACTIVATED) {
            replaceWith(EditorView())
            close()
            Processed
        }

        val buttonsBox = Components.vbox()
            .withSize(15, 10)
            .withSpacing(1)
            .withAlignmentAround(header, ComponentAlignment.BOTTOM_CENTER)
            .build().apply {
                addComponent(newGameButton)
                addComponent(editorButton)
            }

        screen.addComponent(header)
        screen.addComponent(buttonsBox)
    }
}
