package com.collectibleArmy

import org.hexworks.zircon.api.SwingApplications
import com.collectibleArmy.view.StartView

fun main() {
    val app = SwingApplications.startApplication(GameConfig.buildAppConfig())
    val startView = StartView()

    app.dock(startView)
}