package com.indie.apps.pennypal.presentation.ui.component

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.PaymentWithMode
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.MyAppTextField
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.SearchView
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.TopBar
import com.indie.apps.pennypal.presentation.ui.screen.payment.AccountHeadingItem
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.getPaymentModeIcon
import kotlinx.coroutines.launch

@Composable
fun TopBarWithTitle(
    isBackEnable: Boolean = true,
    title: String,
    titleStyle: TextStyle = MyAppTheme.typography.Semibold57,
    onNavigationUp: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.CenterStart,
    bgColor: Color = MyAppTheme.colors.transparent,
    trailingContent: @Composable (() -> Unit)? = null
) {
    TopBar(
        isBackEnable = isBackEnable,
        onBackClick = onNavigationUp,
        content = {
            CustomText(
                text = title, style = titleStyle, color = MyAppTheme.colors.black
            )
        },
        modifier = modifier,
        contentAlignment = contentAlignment,
        bgColor = bgColor,
        trailingContent = trailingContent
    )
}

@Composable
fun UserProfileRound(
    borderWidth: Float = 0f, @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    MyAppTheme.colors.gradientBlue
    val borderModifier = if (borderWidth > 0) {
        Modifier.border(
            BorderStroke(
                width = borderWidth.dp, color = MyAppTheme.colors.gray2
            ), shape = CircleShape
        )
    } else {
        Modifier
    }
    Box(
        modifier = modifier
            .then(borderModifier)
            .size(dimensionResource(id = R.dimen.user_image_bg_size))
            .shadow(
                color = MyAppTheme.colors.brandBg,
                offsetX = (4).dp,
                offsetY = (5).dp,
                blurRadius = 5.dp,
            )
            .background(
                color = MyAppTheme.colors.lightBlue2, shape = CircleShape
            ), contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "User",
            modifier = Modifier.size(dimensionResource(id = R.dimen.user_image_size))/*.graphicsLayer(alpha = 0.99f)
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawRect(linearGradientsBrush(iconGradient), blendMode = BlendMode.SrcAtop)
                }
            }*/,
            tint = MyAppTheme.colors.black
        )
    }
}

@Composable
fun UserProfileRect(
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.Center,
        modifier = modifier
            .roundedCornerBackground(
                MyAppTheme.colors.white, BorderStroke(
                    width = 1.dp,
                    color = MyAppTheme.colors.gray1
                )
            )
            .padding(dimensionResource(R.dimen.button_horizontal_padding))
    ) {
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = "Profile",
            tint = MyAppTheme.colors.gray1
        )
    }
}

@Composable
fun BottomSaveButton(
    @StringRes textId: Int = R.string.save,
    onClick: () -> Unit,
    enabled: Boolean = true,
    @DrawableRes icon: Int? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    PrimaryButton(
        //bgBrush = linearGradientsBrush(MyAppTheme.colors.gradientBlue),
        bgColor = MyAppTheme.colors.buttonBg,
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.button_height)),
        onClick = onClick,
        enabled = enabled
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null)
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "icon",
                    modifier = Modifier.size(25.dp)
                )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding)))
            CustomText(
                text = stringResource(id = textId),
                style = MyAppTheme.typography.Bold49_5,
                color = MyAppTheme.colors.black,
                textAlign = TextAlign.Center,
            )
        }

    }
}

@Composable
fun DialogSearchView(
    searchState: TextFieldState,
    onTextChange: (String) -> Unit,
    trailingContent: @Composable (() -> Unit)? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = modifier.padding(
            horizontal = dimensionResource(id = R.dimen.padding), vertical = 7.dp
        )
    ) {
        SearchView(
            trailingIcon = Icons.Default.Search,
            textState = searchState,
            bgColor = MyAppTheme.colors.lightBlue2,
            modifier = Modifier
                .weight(1f)
                .height(40.dp),
            paddingValues = PaddingValues(
                top = 0.dp,
                bottom = 0.dp,
                start = dimensionResource(id = R.dimen.padding),
                end = 0.dp
            ),
            onTextChange = onTextChange
        )
        Spacer(modifier = Modifier.width(5.dp))
        if (trailingContent != null) {
            trailingContent()
        }
    }
}

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

@Composable
internal fun TextFieldError(textError: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp)
    ) {

        CustomText(
            text = textError,
            modifier = Modifier.weight(1f),
            color = MyAppTheme.colors.redText,
            style = MyAppTheme.typography.Semibold40,
            textAlign = TextAlign.End
        )
    }
}

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

            Row(modifier = Modifier
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

@Composable
fun NoDataMessage(
    title: String,
    details: String,
    painterRes: Int = R.drawable.receipt_long_off,
    iconSize: Dp = 50.dp,
    titleTextStyle: TextStyle = MyAppTheme.typography.Regular51,
    detailsTextStyle: TextStyle = MyAppTheme.typography.Regular44,
    titleColor: Color = MyAppTheme.colors.gray2,
    detailsColor: Color = MyAppTheme.colors.gray3,
    iconColor: Color = MyAppTheme.colors.gray2,
    isClickable: Boolean = false,
    onClick: () -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxSize()
) {
    Box(
        modifier = modifier
            .background(MyAppTheme.colors.transparent)
            .clickableWithNoRipple(enabled = isClickable) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.75f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.item_inner_padding))
        ) {

            Icon(
                painter = painterResource(painterRes),
                contentDescription = "no transaction",
                tint = iconColor,
                modifier = Modifier.size(iconSize)
            )

            CustomText(
                text = title,
                style = titleTextStyle,
                color = titleColor,
                textAlign = TextAlign.Center
            )
            CustomText(
                text = details,
                style = detailsTextStyle,
                color = detailsColor,
                textAlign = TextAlign.Center
            )

        }
    }
}

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

            Icon(imageVector = Icons.Default.Edit,
                contentDescription = "edit",
                modifier = Modifier
                    .roundedCornerBackground(MyAppTheme.colors.transparent)
                    .clickableWithNoRipple { onEditClick() })

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.item_padding)))

            Icon(imageVector = Icons.Default.Delete,
                contentDescription = "delete",
                modifier = Modifier
                    .roundedCornerBackground(MyAppTheme.colors.transparent)
                    .clickableWithNoRipple { onDeleteClick() })
        }
    }
}

@Composable
fun AccountTypeItem(
    @StringRes titleId: Int,
    selectPaymentId: Long,
    editAnimPaymentId: Long = 0L,
    editAnimRun: Boolean = false,
    isSelectable: Boolean,
    isEditable: Boolean,
    dataList: List<PaymentWithMode>,
    onSelect: (PaymentWithMode) -> Unit = {},
    onEditClick: (Long) -> Unit = {},
    onDeleteClick: (PaymentWithMode) -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        val scope = rememberCoroutineScope()
        val baseColor = MyAppTheme.colors.itemBg
        val targetAnimColor = MyAppTheme.colors.lightBlue1

        AccountHeadingItem(titleId)
        Column(
            modifier = modifier
                .fillMaxWidth()
                .roundedCornerBackground(MyAppTheme.colors.itemBg)/*.background(
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
                    color = MyAppTheme.colors.itemBg
                )*/
                .padding(dimensionResource(id = R.dimen.padding))
        ) {

            dataList.forEach { item ->

                val itemAnimateColor = remember {
                    androidx.compose.animation.Animatable(baseColor)
                }
                val modifierColor = if (editAnimPaymentId == item.id && editAnimRun) {

                    scope.launch {
                        itemAnimateColor.animateTo(
                            targetValue = targetAnimColor,
                            animationSpec = tween(Util.EDIT_ITEM_ANIM_TIME)
                        )
                        itemAnimateColor.animateTo(
                            targetValue = baseColor, animationSpec = tween(Util.EDIT_ITEM_ANIM_TIME)
                        )
                    }
                    modifier.background(itemAnimateColor.value)
                } else {
                    Modifier
                }

                AccountItem(
                    isSelected = item.id == selectPaymentId,
                    isSelectable = isSelectable,
                    isEditable = isEditable,
                    name = item.name,
                    symbolId = getPaymentModeIcon(item.name),
                    onSelect = { onSelect(item) },
                    modifier = modifierColor,
                    onDeleteClick = { onDeleteClick(item) },
                    onEditClick = { onEditClick(item.id) },
                    isPreAdded = item.preAdded == 1
                )
            }
        }
    }
}


@Preview
@Composable
private fun MyAppTopBarPreview() {
    PennyPalTheme(darkTheme = true) {
        TopBarWithTitle(
            isBackEnable = true,
            title = "Title",
            onNavigationUp = { },
            contentAlignment = Alignment.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserProfilePreview() {
    PennyPalTheme(darkTheme = true) {
        UserProfileRound()
    }
}

@Preview(showBackground = true)
@Composable
private fun UserProfileRectPreview() {
    PennyPalTheme(darkTheme = true) {
        UserProfileRect()
    }
}

@Preview(showBackground = true)
@Composable
private fun DialogTextFieldItemPreview() {
    PennyPalTheme(darkTheme = true) {
        DialogTextFieldItem(leadingIcon = {},
            placeholder = R.string.amount_placeholder,
            onTextChange = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun NoTransactionMessagePreview() {
    PennyPalTheme(darkTheme = true) {
        NoDataMessage(
            title = "No Transactions Yet",
            details = "Your latest transaction will appear here. Track your spending and income.start now."
        )
    }
}

fun Context.showToast(message: String) = run {
    Toast.makeText(
        this, message, Toast.LENGTH_SHORT
    ).show()
}

@Composable
fun PeriodText(
    text: String,
    textStyle: TextStyle = MyAppTheme.typography.Regular44,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .roundedCornerBackground(MyAppTheme.colors.brand)
                .padding(
                    horizontal = dimensionResource(R.dimen.bottom_bar_item_horizontal_padding),
                    vertical = dimensionResource(R.dimen.bottom_bar_item_vertical_padding)
                )
        ) {
            CustomText(
                text = text, style = textStyle, color = MyAppTheme.colors.black
            )
        }
    }

}

