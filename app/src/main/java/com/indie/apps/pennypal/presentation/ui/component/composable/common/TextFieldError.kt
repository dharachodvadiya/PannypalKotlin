package com.indie.apps.pennypal.presentation.ui.component.composable.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomText
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme


@Composable
internal fun TextFieldError(textError: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp)
    ) {

        CustomText(
            text = textError,
            modifier = Modifier.weight(1f),
            color = MyAppTheme.colors.redText,
            style = MyAppTheme.typography.Semibold40,
            textAlign = TextAlign.End
        )
    }
}
