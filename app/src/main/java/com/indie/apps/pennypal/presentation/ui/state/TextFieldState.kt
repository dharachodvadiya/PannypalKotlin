package com.indie.apps.pennypal.presentation.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.UiText

/*class TextFieldState {
    var text: String by mutableStateOf("")
    var errorText: String by mutableStateOf("")

    private var displayErrors: Boolean by mutableStateOf(false)

    private fun showErrors() = displayErrors

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
}*/

class TextFieldState {
    var text: String by mutableStateOf("")
        private set
    var errorText: UiText by mutableStateOf(UiText.StringResource(R.string.empty))
        private set

    private var displayErrors: Boolean by mutableStateOf(false)

    private fun shouldShowErrors() = displayErrors

    fun getError(): UiText {
        return errorText
    }

    fun setError(newErrorText: UiText) {
        displayErrors = true
        this.errorText = newErrorText
    }

    private fun clearError() {
        displayErrors = false
        this.errorText = UiText.StringResource(R.string.empty)
    }

    fun updateText(newText: String) {
        text = newText
        clearError() // Optionally clear errors when text is updated
    }
}