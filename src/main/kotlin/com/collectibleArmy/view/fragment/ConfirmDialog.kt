package com.collectibleArmy.view.fragment

import com.collectibleArmy.GameConfig
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.builder.component.ModalBuilder
import org.hexworks.zircon.api.component.modal.Modal
import org.hexworks.zircon.api.component.modal.ModalFragment
import org.hexworks.zircon.api.extensions.box
import org.hexworks.zircon.api.extensions.handleComponentEvents
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.ComponentEventType
import org.hexworks.zircon.api.uievent.Processed

class ConfirmDialog(private val screen: Screen) : ModalFragment<ConfirmModalResult> {

    private val container =
        Components.vbox()
            .withDecorations(box(title = "Confirm?"))
            .withSize(30,15)
            .build()

    override val root: Modal<ConfirmModalResult> by lazy {
        ModalBuilder.newBuilder<ConfirmModalResult>()
            .withComponent(container)
            .withParentSize(screen.size)
            .withCenteredDialog(true)
            .build().also {
                container.addComponent(
                    Components.hbox()
                        .withSpacing(1)
                        .build().apply {
                            addComponent(
                                Components.button()
                                    .withText("Confirm")
                                    .build()
                                    .apply {
                                        handleComponentEvents(ComponentEventType.ACTIVATED) {
                                            root.close(ConfirmModalResult(true))
                                            Processed
                                        }
                                    })
                            addComponent(
                                Components.button()
                                    .withText("Cancel")
                                    .build()
                                    .apply {
                                        handleComponentEvents(ComponentEventType.ACTIVATED) {
                                            root.close(ConfirmModalResult(false))
                                            Processed
                                        }
                                    }
                            )
                        }
                )
                container.applyColorTheme(GameConfig.THEME)
            }
    }
}