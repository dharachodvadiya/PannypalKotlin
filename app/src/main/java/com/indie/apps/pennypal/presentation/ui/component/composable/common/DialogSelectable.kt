package com.indie.apps.pennypal.presentation.ui.component.composable.common

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomText
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme


@Composable
fun DialogSelectableItem(
    imageVector: ImageVector? = null,
    text: String? = null,
    errorText: String = "",
    onClick: () -> Unit,
    isSelectable: Boolean = true,
    @StringRes label: Int? = null,
    @StringRes placeholder: Int,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = modifier.padding(
            vertical = 5.dp
        )
    ) {
        label?.let {
            CustomText(
                text = stringResource(id = label),
                style = MyAppTheme.typography.Medium46,
                color = MyAppTheme.colors.gray1
            )
            Spacer(modifier = Modifier.height(5.dp))
        }

        Row(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.new_entry_field_height))
                .roundedCornerBackground(MyAppTheme.colors.transparent),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            imageVector?.let {
                Icon(imageVector = it, contentDescription = "")
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.item_content_padding)))
            }

            Row(
                modifier = Modifier
                    .roundedCornerBackground(MyAppTheme.colors.itemBg)
                    .clickableWithNoRipple { onClick() }
                    .padding(dimensionResource(id = R.dimen.padding)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {

                if (leadingContent != null) {
                    leadingContent()
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.item_content_padding)))
                }

                if (text.isNullOrEmpty()) {
                    CustomText(
                        text = stringResource(id = placeholder),
                        modifier = Modifier.weight(1f),
                        style = MyAppTheme.typography.Regular46,
                        color = MyAppTheme.colors.gray2.copy(alpha = 0.7f)
                    )
                } else {
                    CustomText(
                        text = text,
                        modifier = Modifier.weight(1f),
                        style = MyAppTheme.typography.Medium46,
                        color = MyAppTheme.colors.black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (trailingContent != null) {
                    Spacer(modifier = Modifier.weight(1f))
                    trailingContent()
                }
            }
        }
        TextFieldError(
            textError = errorText
        )

    }

}
