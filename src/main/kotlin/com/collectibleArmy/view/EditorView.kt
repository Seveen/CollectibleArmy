package com.collectibleArmy.view

import com.collectibleArmy.GameConfig
import com.collectibleArmy.army.*
import com.collectibleArmy.army.templating.HeroTemplate
import com.collectibleArmy.army.templating.SoldierTemplate
import com.collectibleArmy.army.templating.UnitTemplate
import com.collectibleArmy.blocks.GameBlock
import com.collectibleArmy.builders.GameTileRepository
import com.collectibleArmy.events.GameLogEvent
import com.collectibleArmy.extensions.isWithin
import com.collectibleArmy.extensions.whenTypeIs
import com.collectibleArmy.functions.logGameEvent
import com.collectibleArmy.game.Game
import com.collectibleArmy.game.GameBuilder
import com.collectibleArmy.view.fragment.editor.LoadArmyDialog
import com.collectibleArmy.view.fragment.editor.SaveArmyDialog
import com.collectibleArmy.view.fragment.editor.initiativePanel.InitiativePanelFragment
import com.collectibleArmy.view.fragment.editor.unitsPanel.UnitsPanelFragment
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.GameComponents
import org.hexworks.zircon.api.Layers
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.extensions.alignmentWithin
import org.hexworks.zircon.api.extensions.handleMouseEvents
import org.hexworks.zircon.api.extensions.onClosed
import org.hexworks.zircon.api.extensions.processComponentEvents
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.mvc.base.BaseView
import org.hexworks.zircon.api.uievent.ComponentEventType
import org.hexworks.zircon.api.uievent.MouseEventType
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.internal.Zircon

class EditorView(private val game: Game = GameBuilder.defaultEditorGame()) : BaseView() {

    override val theme = GameConfig.THEME

    private var selectedEntity: UnitTemplate? = null
    private var hero: HeroHolder? = null
    private var soldiers = mutableListOf<SoldierHolder>()

    private lateinit var initiativePanel: InitiativePanelFragment

    private val highlightLayer = Layers.newBuilder()
        .withSize(game.area.actualSize().to2DSize())
        .build().also {
            game.area.pushOverlayAt(it, 0)
        }

    override fun onDock() {

        val gameComponent = GameComponents.newGameComponentBuilder<Tile, GameBlock>()
            .withGameArea(game.area)
            .withVisibleSize(game.area.visibleSize())
            .withProjectionMode(ProjectionMode.TOP_DOWN)
            .withAlignmentWithin(screen, ComponentAlignment.CENTER)
            .build()
        screen.addComponent(gameComponent)

        val unitsPanel = UnitsPanelFragment(
            22, 40,
            alignmentWithin(screen, ComponentAlignment.TOP_LEFT),
            UnitsRepository.soldiersList,
            UnitsRepository.heroesList
        ) {
            selectedEntity = it
        }
        screen.addFragment(unitsPanel)

        initiativePanel = InitiativePanelFragment( 20, 50 - GameConfig.LOG_AREA_HEIGHT,
            alignmentWithin(screen, ComponentAlignment.TOP_RIGHT),
            ::onHighlightUnit,
            ::onStopHighlightingUnit,
            hero,
            soldiers
        )
        screen.addFragment(initiativePanel)

        val commandsPanel = buildCommandButtonsPanel()
        screen.addComponent(commandsPanel)

        val logArea = Components.logArea()
            .withDecorations(org.hexworks.zircon.api.extensions.box(title = "Log"))
            .withSize(GameConfig.WINDOW_WIDTH - 22, GameConfig.LOG_AREA_HEIGHT)
            .withAlignmentWithin(screen, ComponentAlignment.BOTTOM_RIGHT)
            .build()
        screen.addComponent(logArea)

        Zircon.eventBus.subscribe<GameLogEvent> { (text) ->
            logArea.addParagraph(
                paragraph = text,
                withNewLine = false,
                withTypingEffectSpeedInMs = 10
            )
        }

        gameComponent.handleMouseEvents(MouseEventType.MOUSE_PRESSED) { event, _ ->
            val area = game.area

            val clickPosition = area.screenToWorldPosition(event.position, gameComponent.position)
            if (clickPosition.isWithin(Position.create(1,1), Position.create(5,5))) {
                if (isPositionOccupied(clickPosition)) {
                    removeSoldierAtPosition(clickPosition)
                    refreshGameComponent()
                } else {
                    selectedEntity?.whenTypeIs<HeroTemplate> {
                        hero = HeroHolder(it as HeroTemplate, clickPosition, 1)
                        refreshGameComponent()
                    }

                    selectedEntity?.whenTypeIs<SoldierTemplate> {
                        if (hero != null) {
                            soldiers.add(SoldierHolder(it as SoldierTemplate, clickPosition, soldiers.size + 2))
                            refreshGameComponent()
                        } else {
                            logGameEvent("Select a hero first!")
                        }
                    }
                }
            }

            Processed
        }
    }

    private fun onLoadArmy(name: String) {
        val army = ArmySaver.loadArmy(name)
        army?.heroHolder?.let {
            hero = it
        }
        army?.troopHolders?.let {
            soldiers = it.toMutableList()
        }
        refreshGameComponent()
    }

    private fun onDeleteArmy(name: String) {
        ArmySaver.deleteArmy(name)
    }

    private fun refreshGameComponent() {
        hero?.let {
            game.area.rebuildAreaWithArmies(Army(it, soldiers), null)
        }
        initiativePanel.rebuildWith(hero, soldiers)
    }

    private fun isPositionOccupied(position: Position) : Boolean {
        var result = false
        soldiers.map {
            result = result.or(position == it.initialPosition)
        }
        hero?.let {
            result = result.or(it.initialPosition == position)
        }

        return result
    }

    private fun removeSoldierAtPosition(position: Position) {
        val occupier = soldiers.find {
            position == it.initialPosition
        }
        occupier?.let {
            soldiers.remove(it)
        }
    }

    private fun onHighlightUnit(position: Position) {
        highlightLayer.setTileAt(position, GameTileRepository.UNIT_HIGHLIGHT)
    }

    private fun onStopHighlightingUnit(position: Position) {
        highlightLayer.setTileAt(position, Tile.empty())
    }

    private fun buildCommandButtonsPanel(): Panel {
        val commandsPanel = Components.panel()
            .withSize(22, 10)
            .withAlignmentWithin(screen, ComponentAlignment.BOTTOM_LEFT)
            .withDecorations(box())
            .build()

        val playButton = Components.button()
            .withText("Test")
            .build()
        playButton.processComponentEvents(ComponentEventType.ACTIVATED) {
            if (hero != null) {
                replaceWith(PlayView(game))
                close()
            } else {
                logGameEvent("Select a hero first!")
            }
        }

        val saveButton = Components.button()
            .withText("Save")
            .build()
        saveButton.processComponentEvents(ComponentEventType.ACTIVATED) {
            hero?.let {hero ->
                val modal = SaveArmyDialog(screen).apply {
                    root.onClosed {
                        if (it.result != "") {
                            ArmySaver.saveArmy(Army(hero, soldiers), it.result)
                        }
                    }
                }
                screen.openModal(modal)
            }
        }

        val loadButton = Components.button()
            .withText("Load")
            .build()
        loadButton.processComponentEvents(ComponentEventType.ACTIVATED) {
            val modal = LoadArmyDialog(screen,
                ArmySaver.getArmyList(),
                ::onLoadArmy,
                ::onDeleteArmy
            )
            screen.openModal(modal)
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
                addComponent(saveButton)
                addComponent(loadButton)
                addComponent(playButton)
                addComponent(returnButton)
            }
        commandsPanel.addComponent(buttonsHolder)

        return commandsPanel
    }
}