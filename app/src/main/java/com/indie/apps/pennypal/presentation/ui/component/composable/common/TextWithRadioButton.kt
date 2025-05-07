package com.indie.apps.pennypal.presentation.ui.component.composable.common

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomText
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme


@Composable
fun TextWithRadioButton(
    isSelected: Boolean = false,
    name: String,
    onSelect: () -> Unit,
    @DrawableRes symbolId: Int? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(40.dp)
            .fillMaxWidth()
            .roundedCornerBackground(MyAppTheme.colors.transparent)
            .clickableWithNoRipple { onSelect() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.item_padding))
    ) {
        RadioButton(selected = isSelected, onClick = onSelect)
        if (symbolId != null) {
            Icon(
                painter = painterResource(symbolId),
                contentDescription = "bank",
                tint = MyAppTheme.colors.lightBlue1,
                modifier = Modifier.size(dimensionResource(id = R.dimen.small_icon_size))
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.item_padding)))
        }

        CustomText(
            text = name, style = MyAppTheme.typography.Semibold52_5, color = MyAppTheme.colors.black
        )
    }
}