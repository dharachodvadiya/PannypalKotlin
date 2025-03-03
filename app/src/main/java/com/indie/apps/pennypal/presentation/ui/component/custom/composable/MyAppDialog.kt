package com.indie.apps.pennypal.presentation.ui.component.custom.composable

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pennypal.presentation.ui.component.DialogTextFieldItem
import com.indie.apps.pennypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.component.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun MyAppDialog(
    @StringRes title: Int,
    isBackEnable: Boolean = false,
    onNavigationUp: () -> Unit,
    content: @Composable () -> Unit,
    bottomContent: @Composable (() -> Unit)? = null,
    isFixHeight: Boolean = false,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        verticalArrangement = Arrangement.Bottom
    ) {
        val heightModifier = if (isFixHeight) {
            Modifier.height(dimensionResource(id = R.dimen.dialog_max_height))
        } else {
            Modifier.heightIn(
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
            val spaceModifier = if (isFixHeight) Modifier.weight(1f) else Modifier

            Column(modifier = spaceModifier) {
                content()
            }
            /*
                        if (isFixHeight) {
                            Spacer(modifier = Modifier.weight(1f))
                        }*/
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
                        .roundedCornerBackground(MyAppTheme.colors.transparent)
                        .clickableWithNoRipple {
                            onNavigationUp()
                        })
        },
        contentAlignment = if (isBackEnable) Alignment.Center else Alignment.CenterStart
    )
}

@Preview
@Composable
private fun MyAppDialogPreview() {
    PennyPalTheme(darkTheme = true) {
        MyAppDialog(title = R.string.add_merchant, onNavigationUp = {}, content = {
            DialogTextFieldItem(
                leadingIcon = {},
                placeholder = R.string.merchant_name_placeholder,
                onTextChange = {}
            )
        }, bottomContent = {
            BottomSaveButton(onClick = {})
        })
    }
}

