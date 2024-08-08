package com.indie.apps.pannypal.presentation.ui.dialog

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pannypal.presentation.ui.component.dialog.CppDialogField
import com.indie.apps.pannypal.presentation.ui.state.TextFieldState
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme
import com.indie.apps.cpp.utils.Country

@SuppressLint("RememberReturnType")
@Composable
fun DialogCpp(
    onNavigationUp: () -> Unit,
    onSelect: (Country) -> Unit,
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
                searchState = searchTextState
            )
        },
        modifier = modifier,
        isFixHeight = true
    )
}

@Preview
@Composable
private fun DialogCppPreview() {
    PannyPalTheme(darkTheme = true) {
        //DialogCpp({},{})
    }
}