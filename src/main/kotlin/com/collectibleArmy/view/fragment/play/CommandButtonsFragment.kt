package com.collectibleArmy.view.fragment.play

import com.collectibleArmy.events.*
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.extensions.processComponentEvents
import org.hexworks.zircon.api.graphics.Symbols
import org.hexworks.zircon.api.uievent.ComponentEventType
import org.hexworks.zircon.internal.Zircon

class CommandButtonsFragment : Fragment {

    private val retreatButton = Components.button()
        .withText("${Symbols.ARROW_LEFT}")
        .build().apply {
            processComponentEvents(ComponentEventType.ACTIVATED) {
                Zircon.eventBus.publish(RetreatActionEvent())
            }
        }

    private val forwardButton = Components.button()
        .withText("${Symbols.ARROW_RIGHT}")
        .build().apply {
            processComponentEvents(ComponentEventType.ACTIVATED) {
                Zircon.eventBus.publish(ForwardActionEvent())
            }
        }

    private val attackButton = Components.button()
        .withText("${Symbols.ARROW_UP_DOWN_WITH_BASE}")
        .build().apply {
            processComponentEvents(ComponentEventType.ACTIVATED) {
                Zircon.eventBus.publish(AttackActionEvent())
            }
        }

    private val defenseButton = Components.button()
        .withText("${Symbols.DOUBLE_LINE_CROSS}")
        .build().apply {
            processComponentEvents(ComponentEventType.ACTIVATED) {
                Zircon.eventBus.publish(DefendActionEvent())
            }
        }

    private val castButton = Components.button()
        .withText("${Symbols.ALPHA}")
        .build().apply {
            processComponentEvents(ComponentEventType.ACTIVATED) {
                Zircon.eventBus.publish(CastActionEvent())
            }
        }

    override val root = Components.hbox()
        .withSpacing(1)
        .withSize(20,1)
        .build().apply {
            addComponent(retreatButton)
            addComponent(forwardButton)
            addComponent(attackButton)
            addComponent(defenseButton)
            addComponent(castButton)
        }
}