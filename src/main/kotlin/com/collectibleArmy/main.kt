package com.collectibleArmy

import com.collectibleArmy.view.StartView
import org.hexworks.zircon.api.SwingApplications

fun main() {
    val app = SwingApplications.startApplication(GameConfig.buildAppConfig())
    val startView = StartView()

    app.dock(startView)
}
