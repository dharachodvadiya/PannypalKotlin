package com.indie.apps.pennypal.presentation.ui.component

internal fun String.capitalizeFirstChar(): String {
    return if (this.isNotEmpty()) {
        this[0].uppercase() + this.substring(1)
    } else {
        this
    }
}
