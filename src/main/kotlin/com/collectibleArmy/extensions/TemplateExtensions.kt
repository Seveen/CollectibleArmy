package com.collectibleArmy.extensions

import com.collectibleArmy.army.templating.UnitTemplate
import kotlin.reflect.full.isSubclassOf

inline fun <reified T : UnitTemplate> UnitTemplate.whenTypeIs(fn: (UnitTemplate) -> Unit) {
    if (this::class.isSubclassOf(T::class)) {
        fn(this)
    }
}