package com.collectibleArmy.extensions

import com.collectibleArmy.army.templating.Template
import kotlin.reflect.full.isSubclassOf

inline fun <reified T : Template> Template.whenTypeIs(fn: (Template) -> Unit) {
    if (this::class.isSubclassOf(T::class)) {
        fn(this)
    }
}