package com.indie.apps.pennypal.util

import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.UiText

enum class AppLanguage(
    val title: Int,
    val languageCode: UiText,
    val index: Int
) {

    ENGLISH(R.string.english, UiText.StringResource(R.string.en), 1),
    HINDI(R.string.hindi, UiText.StringResource(R.string.hi), 2),
    GUJARATI(R.string.gujarati, UiText.StringResource(R.string.gu), 3);

    companion object {
        fun fromIndex(index: Int): AppLanguage? {
            return entries.find { it.index == index }
        }
    }
}
