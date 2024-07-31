package com.indie.apps.pannypal.presentation.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue

class TextFieldState(
) {
    var text: String by mutableStateOf("")
    var errorText: String by mutableStateOf("")

    private var displayErrors: Boolean by mutableStateOf(false)

    fun showErrors() = displayErrors

    fun getError(): String {
        return if (showErrors()) {
            errorText
        } else {
            ""
        }
    }

    fun setError(errorText: String){
        displayErrors = true
        this.errorText = errorText
    }

    fun disableError(){
        displayErrors = false
        this.errorText = ""
    }
}