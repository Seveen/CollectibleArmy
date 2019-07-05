package com.collectibleArmy.view

import com.collectibleArmy.GameConfig
import com.collectibleArmy.KeyboardMapping.AttackKey
import com.collectibleArmy.KeyboardMapping.DefendKey
import com.collectibleArmy.KeyboardMapping.ForwardKey
import com.collectibleArmy.KeyboardMapping.RetreatKey
import com.collectibleArmy.attributes.types.BlueFaction
import com.collectibleArmy.blocks.GameBlock
import com.collectibleArmy.command.globals.GlobalCommand
import com.collectibleArmy.commands.globals.Attack
import com.collectibleArmy.commands.globals.Defend
import com.collectibleArmy.commands.globals.Forward
import com.collectibleArmy.commands.globals.Retreat
import com.collectibleArmy.events.ForwardActionEvent
import com.collectibleArmy.game.Game
import com.collectibleArmy.game.GameBuilder
import com.collectibleArmy.view.fragment.CommandButtonsFragment
import com.collectibleArmy.events.AttackActionEvent
import com.collectibleArmy.events.DefendActionEvent
import com.collectibleArmy.events.GameLogEvent
import com.collectibleArmy.events.RetreatActionEvent
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.GameComponents
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.extensions.box
import org.hexworks.zircon.api.extensions.handleKeyboardEvents
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.mvc.base.BaseView
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.internal.Zircon

class PlayView(private val game: Game = GameBuilder.defaultGame()) : BaseView() {

    override val theme = GameConfig.THEME

    override fun onDock() {
        val logArea = Components.logArea()
            .withDecorations(box(title = "Log"))
            .withSize(GameConfig.WINDOW_WIDTH, GameConfig.LOG_AREA_HEIGHT)
            .withAlignmentWithin(screen, ComponentAlignment.BOTTOM_RIGHT)
            .build()
        screen.addComponent(logArea)

        val gameComponent = GameComponents.newGameComponentBuilder<Tile, GameBlock>()
            .withGameArea(game.area)
            .withVisibleSize(game.area.visibleSize())
            .withProjectionMode(ProjectionMode.TOP_DOWN)
            .withAlignmentWithin(screen, ComponentAlignment.CENTER)
            .build()
        screen.addComponent(gameComponent)

        val actionsPanel = Components.panel()
            .withSize(17, 3)
            .withAlignmentAround(gameComponent, ComponentAlignment.BOTTOM_CENTER)
            .withDecorations(box())
            .build().apply {
                addFragment(CommandButtonsFragment())
            }
        screen.addComponent(actionsPanel)

        Zircon.eventBus.subscribe<GameLogEvent> { (text) ->
            logArea.addParagraph(
                paragraph = text,
                withNewLine = false,
                withTypingEffectSpeedInMs = 10
            )
        }

        screen.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, _ ->
            var command: GlobalCommand? = null

            when (event.code) {
                ForwardKey -> command = Forward(BlueFaction)
                RetreatKey -> command = Retreat(BlueFaction)
                AttackKey -> command = Attack(BlueFaction)
                DefendKey -> command = Defend(BlueFaction)
            }

            if (command != null) {
                game.area.update(screen, command, game)
            }

            Processed
        }

        Zircon.eventBus.subscribe<AttackActionEvent> {
            game.area.update(screen,
                Attack(BlueFaction),
                game)
        }

        Zircon.eventBus.subscribe<DefendActionEvent> {
            game.area.update(screen,
                Defend(BlueFaction),
                game)
        }

        Zircon.eventBus.subscribe<ForwardActionEvent> {
            game.area.update(screen,
                Forward(BlueFaction),
                game)
        }

        Zircon.eventBus.subscribe<RetreatActionEvent> {
            game.area.update(screen,
                Retreat(BlueFaction),
                game)
        }
    }
}
