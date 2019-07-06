package com.collectibleArmy.view

import com.collectibleArmy.GameConfig
import com.collectibleArmy.army.Army
import com.collectibleArmy.army.HeroHolder
import com.collectibleArmy.army.SoldierHolder
import com.collectibleArmy.army.templating.HeroTemplate
import com.collectibleArmy.army.templating.SoldierTemplate
import com.collectibleArmy.army.templating.Template
import com.collectibleArmy.army.templating.TemplateLoading
import com.collectibleArmy.attributes.types.BlueFaction
import com.collectibleArmy.blocks.GameBlock
import com.collectibleArmy.events.GameLogEvent
import com.collectibleArmy.extensions.whenTypeIs
import com.collectibleArmy.functions.logGameEvent
import com.collectibleArmy.game.Game
import com.collectibleArmy.game.GameBuilder
import com.collectibleArmy.view.fragment.editor.UnitsPanelFragment
import com.collectibleArmy.view.fragment.editor.UnitsPanelTabsButtonsFragment
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.GameComponents
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.extensions.handleMouseEvents
import org.hexworks.zircon.api.extensions.processComponentEvents
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.mvc.base.BaseView
import org.hexworks.zircon.api.uievent.ComponentEventType
import org.hexworks.zircon.api.uievent.MouseEventType
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.internal.Zircon

class EditorView(private val game: Game = GameBuilder.defaultEditorGame()) : BaseView() {

    override val theme = GameConfig.THEME

    private var selectedEntity: Template? = null

    private val templateLoader = TemplateLoading()
    private val soldiersList = templateLoader.loadSoldierTemplates()
    private val heroesList = templateLoader.loadHeroTemplates()

    private var displayedList = listOf<Template>()

    private var hero: HeroHolder? = null
    private var soldiers = mutableListOf<SoldierHolder>()

    override fun onDock() {
        displayedList = heroesList

        val gameComponent = GameComponents.newGameComponentBuilder<Tile, GameBlock>()
            .withGameArea(game.area)
            .withVisibleSize(game.area.visibleSize())
            .withProjectionMode(ProjectionMode.TOP_DOWN)
            .withAlignmentWithin(screen, ComponentAlignment.CENTER)
            .build()
        screen.addComponent(gameComponent)

        val unitsPanel = Components.panel()
            .withSize(22, 50)
            .withAlignmentWithin(screen, ComponentAlignment.LEFT_CENTER)
            .withDecorations(box())
            .build()
        rebuildUnitsPanel(unitsPanel)
        screen.addComponent(unitsPanel)

        val detailsPanel = Components.panel()
            .withSize(20, 50 - GameConfig.LOG_AREA_HEIGHT)
            .withAlignmentWithin(screen, ComponentAlignment.TOP_RIGHT)
            .withDecorations(box())
            .build()
        screen.addComponent(detailsPanel)

        val logArea = Components.logArea()
            .withDecorations(org.hexworks.zircon.api.extensions.box(title = "Log"))
            .withSize(GameConfig.WINDOW_WIDTH - 22, GameConfig.LOG_AREA_HEIGHT)
            .withAlignmentWithin(screen, ComponentAlignment.BOTTOM_RIGHT)
            .build()
        screen.addComponent(logArea)

        val playButton = Components.button()
            .withText("Play")
            .withAlignmentWithin(detailsPanel, ComponentAlignment.BOTTOM_RIGHT)
            .build()
        playButton.processComponentEvents(ComponentEventType.ACTIVATED) {
            if (hero != null) {
                replaceWith(PlayView(game))
                close()
            } else {
                logGameEvent("Select a hero first!")
            }

            Processed
        }
        detailsPanel.addComponent(playButton)

        Zircon.eventBus.subscribe<GameLogEvent> { (text) ->
            logArea.addParagraph(
                paragraph = text,
                withNewLine = false,
                withTypingEffectSpeedInMs = 10
            )
        }

        gameComponent.handleMouseEvents(MouseEventType.MOUSE_PRESSED) { event, _ ->
            val area = game.area
            //TODO : restrain click to 1:1 5:5
            val clickPosition = area.screenToWorldPosition(event.position, gameComponent.position)
            println(clickPosition)

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

            Processed
        }
    }

    private fun refreshGameComponent() {
        hero?.let {
            game.area.rebuildAreaWithArmy(Army(it, soldiers, BlueFaction))
        }
    }

    private fun handleChangeDisplayedList(list: List<Template>, panel: Panel) {
        displayedList = list
        rebuildUnitsPanel(panel)
    }

    private fun rebuildUnitsPanel(panel: Panel) {
        panel.detachAllComponents()
        val list = Components.vbox()
            .withSpacing(1)
            .withSize(20, 48)
            .build().apply {
                addFragment(UnitsPanelTabsButtonsFragment(20).apply {
                    heroesButton.processComponentEvents(ComponentEventType.ACTIVATED) {
                        handleChangeDisplayedList(heroesList, panel)
                    }
                    soldiersButton.processComponentEvents(ComponentEventType.ACTIVATED) {
                        handleChangeDisplayedList(soldiersList, panel)
                    }
                })
                addFragment(UnitsPanelFragment(displayedList, 19, onSelectUnit = {
                    selectedEntity = it
                }))
            }
        panel.addComponent(list)
        panel.applyColorTheme(GameConfig.THEME)
    }
}