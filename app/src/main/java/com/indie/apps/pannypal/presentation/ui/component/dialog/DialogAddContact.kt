package com.indie.apps.pannypal.presentation.ui.component.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pannypal.presentation.ui.component.DialogTextFieldItem
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun DialogAddContact(
    onNavigationUp: ()-> Unit,
    modifier: Modifier = Modifier
) {

    MyAppDialog(
        title = R.string.add_merchant,
        onNavigationUp = onNavigationUp,
        content = {
            AddDialogField()
        },
        bottomContent = {
            BottomSaveButton(
                onClick = {},
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding))
            )
        },
        modifier = modifier
    )
}

@Composable
private fun AddDialogField()
{
    Column {
        DialogTextFieldItem(
            imageVector = Icons.Default.PersonOutline,
            placeholder = R.string.merchant_name_placeholder)
        DialogTextFieldItem(
            imageVector = Icons.Default.Details,
            placeholder = R.string.description_placeholder)
    }
}

@Preview()
@Composable
private fun MyAppDialogPreview() {
    PannyPalTheme {
        DialogAddContact({})
    }
}