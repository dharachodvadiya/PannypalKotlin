package com.indie.apps.pannypal.presentation.ui.dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pannypal.presentation.ui.component.dialog.AddPaymentDialogField
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun DialogAddPayment(
    onNavigationUp: () -> Unit,
    modifier: Modifier = Modifier
) {

    MyAppDialog(
        title = R.string.add_payment,
        onNavigationUp = onNavigationUp,
        content = {
            AddPaymentDialogField()
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

@Preview()
@Composable
private fun MyAppDialogPreview() {
    PannyPalTheme {
        DialogAddPayment({})
    }
}