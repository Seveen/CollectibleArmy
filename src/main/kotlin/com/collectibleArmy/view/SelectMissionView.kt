package com.collectibleArmy.view

import com.collectibleArmy.GameConfig
import com.collectibleArmy.army.Army
import com.collectibleArmy.army.ArmySaver
import com.collectibleArmy.blocks.GameBlock
import com.collectibleArmy.functions.logGameEvent
import com.collectibleArmy.game.Game
import com.collectibleArmy.game.GameBuilder
import com.collectibleArmy.view.fragment.editor.armyList.ArmyListScrollable
import org.hexworks.zircon.api.ComponentDecorations
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.GameComponents
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.extensions.alignmentWithin
import org.hexworks.zircon.api.extensions.processComponentEvents
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.mvc.base.BaseView
import org.hexworks.zircon.api.uievent.ComponentEventType

class SelectMissionView(private val game: Game = GameBuilder.defaultEditorGame()) : BaseView() {

    override val theme = GameConfig.THEME

    private var currentArmy: Army? = null
    private var currentEnemyArmy: Army? = null

    override fun onDock() {

        val armyPanel = ArmyListScrollable(Size.create(20, 50),
            alignmentWithin(screen, ComponentAlignment.LEFT_CENTER),
            ArmySaver.getArmyList(),
            ::onLoadArmy,
            ::onDeleteArmy)

        val enemyPanel = ArmyListScrollable(Size.create(20, 50),
            alignmentWithin(screen, ComponentAlignment.RIGHT_CENTER),
            ArmySaver.getArmyList(),
            ::onLoadEnemyArmy,
            null)

        val gameComponent = GameComponents.newGameComponentBuilder<Tile, GameBlock>()
            .withGameArea(game.area)
            .withVisibleSize(game.area.visibleSize())
            .withProjectionMode(ProjectionMode.TOP_DOWN)
            .withAlignmentWithin(screen, ComponentAlignment.CENTER)
            .build()

        val commandButtonsPanel = buildCommandButtonsPanel()

        screen.addFragment(armyPanel)
        screen.addFragment(enemyPanel)
        screen.addComponent(gameComponent)
        screen.addComponent(commandButtonsPanel)
    }

    private fun onDeleteArmy(name: String) {
        ArmySaver.deleteArmy(name)
    }

    private fun onLoadArmy(name: String) {
        currentArmy = ArmySaver.loadArmy(name)
        refreshGameComponent()
    }

    private fun onLoadEnemyArmy(name: String) {
        currentEnemyArmy = ArmySaver.loadEnemyArmy(name)
        refreshGameComponent()
    }

    private fun refreshGameComponent() {
        game.area.rebuildAreaWithArmies(currentArmy, currentEnemyArmy)
    }

    private fun buildCommandButtonsPanel(): Panel {
        val commandsPanel = Components.panel()
            .withSize(22, 10)
            .withAlignmentWithin(screen, ComponentAlignment.BOTTOM_CENTER)
            .withDecorations(ComponentDecorations.box())
            .build()

        val playButton = Components.button()
            .withText("Play")
            .build()
        playButton.processComponentEvents(ComponentEventType.ACTIVATED) {
            if (currentArmy != null && currentEnemyArmy != null) {
                replaceWith(PlayView(game))
                close()
            } else {
                logGameEvent("Select an army and an opponent first!")
            }
        }

        val returnButton = Components.button()
            .withText("Back")
            .build()
        returnButton.processComponentEvents(ComponentEventType.ACTIVATED) {
            replaceWith(StartView())
            close()
        }

        val buttonsHolder = Components.vbox()
            .withSize(commandsPanel.size.width - 3, 8)
            .withSpacing(1)
            .build().apply {
                addComponent(playButton)
                addComponent(returnButton)
            }
        commandsPanel.addComponent(buttonsHolder)

        return commandsPanel
    }
}