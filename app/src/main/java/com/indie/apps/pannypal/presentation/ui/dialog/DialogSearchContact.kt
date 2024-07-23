package com.indie.apps.pannypal.presentation.ui.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pannypal.presentation.ui.component.dialog.SearchDialogField
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun DialogSearchContact(
    onNavigationUp: () -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    MyAppDialog(
        isBackEnable = true,
        title = R.string.select_merchant,
        onNavigationUp = onNavigationUp,
        content = {
            SearchDialogField(
                onAddClick = onAddClick,
                onTextChange = { }
            )
        },
        modifier = modifier
    )
}

@Preview()
@Composable
private fun MyAppDialogPreview() {
    PannyPalTheme {
        DialogSearchContact({},{})
    }
}