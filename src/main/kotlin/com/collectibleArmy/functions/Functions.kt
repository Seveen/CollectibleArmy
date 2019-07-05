package com.collectibleArmy.functions

import com.collectibleArmy.events.GameLogEvent
import org.hexworks.zircon.internal.Zircon

fun logGameEvent(text: String) {
    Zircon.eventBus.publish(GameLogEvent(text))
}