package com.indie.apps.pennypal.presentation.ui.component.composable.common

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomText
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.MyAppTextField
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme

@Composable
fun DialogTextFieldItem(
    leadingIcon: @Composable (() -> Unit)? = null,
    textState: TextFieldState = TextFieldState(),
    onTextChange: (String) -> Unit,
    placeholder: Int,
    @StringRes label: Int? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    bgColor: Color = MyAppTheme.colors.itemBg,
    isBottomLineEnable: Boolean = false,
    focusRequester: FocusRequester? = null,
    nextFocusRequester: FocusRequester? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    textLeadingContent: @Composable (() -> Unit)? = null,
    textTrailingContent: @Composable (() -> Unit)? = null,
) {
    Column(
        /*modifier = modifier.padding(
            vertical = 5.dp
        )*/
    ) {
        label?.let {
            CustomText(
                text = stringResource(id = label),
                style = MyAppTheme.typography.Medium46,
                color = MyAppTheme.colors.gray1
            )
            Spacer(modifier = Modifier.height(5.dp))
        }
        val colorDivider = MyAppTheme.colors.gray1

        Row(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.new_entry_field_height))
                .roundedCornerBackground(MyAppTheme.colors.transparent),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val bottomLineModifier = if (isBottomLineEnable)
                Modifier.drawBehind {
                    drawLine(
                        colorDivider,
                        Offset(0f, size.height),
                        Offset(size.width, size.height),
                        2f
                    )
                } else Modifier

            leadingIcon?.let {
                //Icon(imageVector = it, contentDescription = "", tint = imageColor)
                leadingIcon()
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.item_content_padding)))
            }
            MyAppTextField(
                value = textState.text,/*onValueChange = {
                    textState.disableError()
                    textState.text = it
                },*/
                onValueChange = onTextChange,
                placeHolder = stringResource(placeholder),
                textStyle = MyAppTheme.typography.Medium46,
                keyboardType = keyboardType,
                textLeadingContent = textLeadingContent,
                trailingIcon = textTrailingContent,
                placeHolderTextStyle = MyAppTheme.typography.Regular46,
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.new_entry_field_height))
                    .then(bottomLineModifier),
                bgColor = bgColor,
                focusRequester = focusRequester,
                nextFocusRequester = nextFocusRequester,
                imeAction = nextFocusRequester?.let { ImeAction.Next } ?: ImeAction.Done,
                paddingValues = PaddingValues(horizontal = dimensionResource(id = R.dimen.item_content_padding))
            )
        }
        TextFieldError(
            textError = textState.getError().asString()
        )
    }

}