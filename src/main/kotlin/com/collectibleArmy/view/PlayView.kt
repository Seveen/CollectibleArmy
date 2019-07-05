package com.collectibleArmy.view

import com.collectibleArmy.GameConfig
import com.collectibleArmy.KeyboardMapping.AttackKey
import com.collectibleArmy.KeyboardMapping.DefendKey
import com.collectibleArmy.KeyboardMapping.ForwardKey
import com.collectibleArmy.KeyboardMapping.RetreatKey
import com.collectibleArmy.attributes.types.BlueFaction
import com.collectibleArmy.attributes.types.RedFaction
import com.collectibleArmy.blocks.GameBlock
import com.collectibleArmy.command.globals.GlobalCommand
import com.collectibleArmy.commands.globals.GlobalAttack
import com.collectibleArmy.commands.globals.GlobalDefend
import com.collectibleArmy.commands.globals.GlobalForward
import com.collectibleArmy.commands.globals.GlobalRetreat
import com.collectibleArmy.events.*
import com.collectibleArmy.game.Game
import com.collectibleArmy.game.GameBuilder
import com.collectibleArmy.view.fragment.play.CommandButtonsFragment
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.GameComponents
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.extensions.box
import org.hexworks.zircon.api.extensions.handleKeyboardEvents
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.mvc.base.BaseView
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.internal.Zircon

class PlayView(val game: Game = GameBuilder.defaultGame()) : BaseView() {

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

            //TODO: Add a "re formation" that tries to replace each pawn at its starting place ???

            when (event.code) {
                ForwardKey -> command = GlobalForward(BlueFaction)
                RetreatKey -> command = GlobalRetreat(BlueFaction)
                AttackKey -> command = GlobalAttack(BlueFaction)
                DefendKey -> command = GlobalDefend(BlueFaction)
                KeyCode.KEY_A -> command = GlobalForward(RedFaction)
                KeyCode.KEY_S -> command = GlobalRetreat(RedFaction)
                KeyCode.KEY_D -> command = GlobalAttack(RedFaction)
                KeyCode.KEY_F -> command = GlobalDefend(RedFaction)
            }

            if (command != null) {
                game.area.update(screen, command, game)
            }

            Processed
        }

        Zircon.eventBus.subscribe<AttackActionEvent> {
            game.area.update(screen,
                GlobalAttack(BlueFaction),
                game)
        }

        Zircon.eventBus.subscribe<DefendActionEvent> {
            game.area.update(screen,
                GlobalDefend(BlueFaction),
                game)
        }

        Zircon.eventBus.subscribe<ForwardActionEvent> {
            game.area.update(screen,
                GlobalForward(BlueFaction),
                game)
        }

        Zircon.eventBus.subscribe<RetreatActionEvent> {
            game.area.update(screen,
                GlobalRetreat(BlueFaction),
                game)
        }
    }
}
