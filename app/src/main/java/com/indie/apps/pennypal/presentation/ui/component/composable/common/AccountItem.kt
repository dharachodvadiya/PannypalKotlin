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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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


@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun AccountItem(
    isSelected: Boolean,
    isSelectable: Boolean,
    isEditable: Boolean,
    isPreAdded: Boolean,
    name: String,
    onSelect: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    @DrawableRes symbolId: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
            .roundedCornerBackground(MyAppTheme.colors.transparent)
            .then(modifier)
            .clickableWithNoRipple(
                enabled = isSelectable || isEditable,
                //interactionSource = MutableInteractionSource(),
                // indication = null
            ) {
                onSelect()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.item_padding))
    ) {
        if (isSelectable) RadioButton(selected = isSelected, onClick = onSelect)
        Icon(
            painter = painterResource(symbolId),
            contentDescription = "bank",
            tint = MyAppTheme.colors.lightBlue1,
            modifier = Modifier.size(dimensionResource(id = R.dimen.small_icon_size))
        )
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.item_padding)))
        CustomText(
            text = name, style = MyAppTheme.typography.Semibold52_5, color = MyAppTheme.colors.black
        )

        if (isEditable && !isPreAdded) {
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "edit",
                modifier = Modifier
                    .roundedCornerBackground(MyAppTheme.colors.transparent)
                    .clickableWithNoRipple { onEditClick() })

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.item_padding)))

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "delete",
                modifier = Modifier
                    .roundedCornerBackground(MyAppTheme.colors.transparent)
                    .clickableWithNoRipple { onDeleteClick() })
        }
    }
}