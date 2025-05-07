package com.indie.apps.pennypal.presentation.ui.component.composable.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme

@Composable
fun CustomProgressDialog(
    message: Int?,
    modifier: Modifier = Modifier
) {

    Dialog(
        onDismissRequest = { },
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .background(MyAppTheme.colors.black, shape = RoundedCornerShape(8.dp))
                .padding(30.dp),
        ) {
            CircularProgressIndicator()

            if (message != null) {
                Spacer(modifier = Modifier.width(15.dp))
                CustomText(
                    text = stringResource(id = message),
                    color = MyAppTheme.colors.white
                )
            }
        }
    }
}