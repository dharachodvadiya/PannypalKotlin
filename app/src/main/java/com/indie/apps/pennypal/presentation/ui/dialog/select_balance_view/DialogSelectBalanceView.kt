package com.indie.apps.pennypal.presentation.ui.dialog.select_balance_view

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun DialogSelectBalanceView(
    selectBalanceViewViewModel: SelectBalanceViewViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onSelect: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    val currentOptionIndex by selectBalanceViewViewModel.currentOptionIndex.collectAsStateWithLifecycle()
    val optionList by selectBalanceViewViewModel.optionList.collectAsStateWithLifecycle()

    MyAppDialog(
        isBackEnable = true,
        title = R.string.select_balance_view,
        onNavigationUp = {
            onNavigationUp()
        },
        content = {
            DialogSelectBalanceViewData(
                optionList = optionList,
                selectedIndex = currentOptionIndex,
                onSelect = {
                    selectBalanceViewViewModel.onSaveOption(it) { onSelect() }
                }
            )

        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun MyAppDialogPreview() {
    PennyPalTheme(darkTheme = true) {
        DialogSelectBalanceView(
            onNavigationUp = {},
            onSelect = { },
        )
    }
}