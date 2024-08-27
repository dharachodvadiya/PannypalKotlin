package com.indie.apps.pennypal.presentation.ui.dialog

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pennypal.presentation.ui.component.dialog.CppDialogField
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.cpp.data.model.Country

@SuppressLint("RememberReturnType")
@Composable
fun DialogCpp(
    onNavigationUp: () -> Unit,
    onSelect: (Country) -> Unit,
    isShowCurrency: Boolean,
    modifier: Modifier = Modifier
) {
    val searchTextState by remember { mutableStateOf(TextFieldState()) }
    MyAppDialog(
        isBackEnable = true,
        title = R.string.select_country,
        onNavigationUp = onNavigationUp,
        content = {
            CppDialogField(
                onSelect = onSelect,
                searchState = searchTextState,
                isShowCurrency = isShowCurrency
            )
        },
        modifier = modifier,
        isFixHeight = true
    )
}

@Preview
@Composable
private fun DialogCppPreview() {
    PennyPalTheme(darkTheme = true) {
        //DialogCpp({},{})
    }
}