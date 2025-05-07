package com.indie.apps.pennypal.presentation.ui.component.extension

import android.content.Context
import android.widget.Toast

fun Context.showToast(message: String) = run {
    Toast.makeText(
        this, message, Toast.LENGTH_SHORT
    ).show()
}