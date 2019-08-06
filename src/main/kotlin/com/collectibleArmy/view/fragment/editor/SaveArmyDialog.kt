package com.collectibleArmy.view.fragment.editor

import com.collectibleArmy.view.fragment.Dialog
import com.collectibleArmy.view.fragment.NameModalResult
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.extensions.box
import org.hexworks.zircon.api.extensions.handleComponentEvents
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.ComponentEventType
import org.hexworks.zircon.api.uievent.Processed


class SaveArmyDialog(screen: Screen) : Dialog<NameModalResult>(screen, false, NameModalResult("")) {

    private val armyNameText = Components.textArea()
        .withSize(23,2)
        .withText("").build()

    override val container = Components.vbox()
        .withDecorations(box(title = "Save Army"))
        .withSize(25,5)
        .build().apply {
            addComponent(
                armyNameText
            )
            addComponent(
                Components.hbox().withSize(23, 1).withSpacing(1).build().apply {
                    addComponent(
                        Components.button()
                            .withText("Confirm")
                            .build().apply {
                                handleComponentEvents(ComponentEventType.ACTIVATED) {
                                    root.close(NameModalResult(armyNameText.textBuffer().getText()))
                                    Processed
                                }
                            }
                    )
                    addComponent(
                        Components.button()
                            .withText("Cancel")
                            .build().apply {
                                handleComponentEvents(ComponentEventType.ACTIVATED) {
                                    root.close(NameModalResult(""))
                                    Processed
                                }
                            }
                    )
                }
            )
        }
}