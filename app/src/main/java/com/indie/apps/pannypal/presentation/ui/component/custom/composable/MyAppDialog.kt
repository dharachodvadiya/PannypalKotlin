package com.indie.apps.pannypal.presentation.ui.component.custom.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pannypal.presentation.ui.component.DialogTextFieldItem
import com.indie.apps.pannypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun MyAppDialog(
    @StringRes title: Int,
    isBackEnable: Boolean = false,
    onNavigationUp: () -> Unit,
    content: @Composable () -> Unit,
    bottomContent: @Composable (() -> Unit)? = null,
    isFixHeight: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.weight(1f))

        val heightModifier = if (isFixHeight) {
            Modifier.height(dimensionResource(id = R.dimen.dialog_max_height))
        } else {
            Modifier
                .heightIn(
                    min = dimensionResource(id = R.dimen.dialog_min_height),
                    max = dimensionResource(id = R.dimen.dialog_max_height)
                )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .then(heightModifier)
                .background(
                    color = MyAppTheme.colors.bottomBg,
                    shape = RoundedCornerShape(topStartPercent = 7, topEndPercent = 7)
                )
        ) {
            DialogTopBar(
                isBackEnable = isBackEnable,
                title = title,
                onNavigationUp = {
                    onNavigationUp()
                }
            )

            content()

            if (isFixHeight) {
                Spacer(modifier = Modifier.weight(1f))
            }
            if (bottomContent != null) {
                bottomContent()
            }

        }
    }

}

@Composable
private fun DialogTopBar(
    @StringRes title: Int,
    isBackEnable: Boolean = false,
    onNavigationUp: () -> Unit,
) {
    TopBarWithTitle(
        isBackEnable = isBackEnable,
        title = stringResource(title),
        titleStyle = MyAppTheme.typography.Semibold54,
        onNavigationUp = onNavigationUp,
        bgColor = MyAppTheme.colors.transparent,
        trailingContent = {
            if (!isBackEnable)
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "close",
                    modifier = Modifier
                        .clickable {
                            onNavigationUp()
                        }
                )
        },
        contentAlignment = if (isBackEnable) Alignment.Center else Alignment.CenterStart
    )
}

@Preview
@Composable
private fun MyAppDialogPreview() {
    PannyPalTheme(darkTheme = true) {
        MyAppDialog(
            title = R.string.add_merchant,
            onNavigationUp = {},
            content = {
                DialogTextFieldItem(
                    imageVector = Icons.Default.PersonOutline,
                    placeholder = R.string.merchant_name_placeholder
                )
            },
            bottomContent = {
                BottomSaveButton(
                    onClick = {}
                )
            })
    }
}