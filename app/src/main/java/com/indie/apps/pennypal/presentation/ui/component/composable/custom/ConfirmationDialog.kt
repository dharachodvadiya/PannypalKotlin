package com.indie.apps.pennypal.presentation.ui.component.composable.custom

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme

@Composable
fun ConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: Int,
    dialogText: Int,
    @StringRes positiveText: Int = R.string.confirm,
    @StringRes negativeText: Int = R.string.dismiss
) {
    AlertDialog(title = {
        CustomText(
            text = stringResource(id = dialogTitle),
            style = MyAppTheme.typography.Semibold57,
            color = MyAppTheme.colors.black
        )
    }, text = {
        CustomText(
            text = stringResource(id = dialogText),
            style = MyAppTheme.typography.Regular46,
            color = MyAppTheme.colors.gray2
        )
    }, onDismissRequest = {
        onDismissRequest()
    }, confirmButton = {
        TextButton(onClick = {
            onConfirmation()
        }) {
            CustomText(stringResource(id = positiveText))
        }
    }, dismissButton = {
        TextButton(onClick = {
            onDismissRequest()
        }) {
            CustomText(stringResource(id = negativeText))
        }
    })
}