package com.collectibleArmy.view.fragment

import com.collectibleArmy.GameConfig
import org.hexworks.zircon.api.builder.component.ModalBuilder
import org.hexworks.zircon.api.component.Container
import org.hexworks.zircon.api.component.modal.Modal
import org.hexworks.zircon.api.component.modal.ModalFragment
import org.hexworks.zircon.api.component.modal.ModalResult
import org.hexworks.zircon.api.screen.Screen


abstract class Dialog<T: ModalResult>(
    private val screen: Screen,
    withClose: Boolean = true,
    result: T) : ModalFragment<T> {

    abstract val container: Container

    final override val root: Modal<T> by lazy {
        ModalBuilder.newBuilder<T>()
            .withComponent(container)
            .withParentSize(screen.size)
            .withCenteredDialog(true)
            .build().also {
                if (withClose) {
                    container.addFragment(CloseButtonFragment(it, container, result))
                }
                container.applyColorTheme(GameConfig.THEME)
            }
    }
}