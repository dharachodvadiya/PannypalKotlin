package com.indie.apps.pennypal.presentation.ui.component

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.PaymentWithMode
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.MyAppTextField
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.SearchView
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.TopBar
import com.indie.apps.pennypal.presentation.ui.screen.payment.AccountHeadingItem
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Util
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
            Text(
                text = title,
                style = titleStyle,
                color = MyAppTheme.colors.black
            )
        },
        modifier = modifier,
        contentAlignment = contentAlignment,
        bgColor = bgColor,
        trailingContent = trailingContent
    )
}

@Composable
fun UserProfile(
    borderWidth: Float = 0f,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    MyAppTheme.colors.gradientBlue
    val borderModifier = if (borderWidth > 0) {
        Modifier
            .border(
                BorderStroke(
                    width = borderWidth.dp,
                    color = MyAppTheme.colors.gray2
                ),
                shape = CircleShape
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
                color = MyAppTheme.colors.lightBlue2,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "User",
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.user_image_size))
            /*.graphicsLayer(alpha = 0.99f)
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
fun BottomSaveButton(
    @StringRes textId: Int = R.string.save,
    onClick: () -> Unit,
    enabled: Boolean = true,
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
        Text(
            text = stringResource(id = textId),
            style = MyAppTheme.typography.Bold49_5,
            color = MyAppTheme.colors.black,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun DialogSearchView(
    searchState: TextFieldState,
    onTextChange: (String) -> Unit,
    trailingContent: @Composable() (() -> Unit)? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(
                horizontal = dimensionResource(id = R.dimen.padding),
                vertical = 7.dp
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
    imageVector: ImageVector,
    textState: TextFieldState = TextFieldState(),
    placeholder: Int,
    keyboardType: KeyboardType = KeyboardType.Text,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    textLeadingContent: @Composable (() -> Unit)? = null,
    textTrailingContent: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .padding(
                horizontal = dimensionResource(id = R.dimen.padding),
                vertical = 5.dp
            )
    ) {
        Row(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.new_entry_field_height))
                .roundedCornerBackground(MyAppTheme.colors.transparent)
               /* .background(
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
                    color = MyAppTheme.colors.transparent
                )*/,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Icon(imageVector = imageVector, contentDescription = "")
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.item_content_padding)))
            MyAppTextField(
                value = textState.text,
                onValueChange = {
                    textState.disableError()
                    textState.text = it
                },
                placeHolder = stringResource(placeholder),
                textStyle = MyAppTheme.typography.Medium46,
                keyboardType = keyboardType,
                textLeadingContent = textLeadingContent,
                trailingIcon = textTrailingContent,
                placeHolderTextStyle = MyAppTheme.typography.Regular46,
                modifier = Modifier.height(dimensionResource(id = R.dimen.new_entry_field_height)),
                bgColor = MyAppTheme.colors.itemBg,
                paddingValues = PaddingValues(horizontal = dimensionResource(id = R.dimen.item_content_padding))
            )
        }
        TextFieldError(
            textError = textState.getError()
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

        Text(
            text = textError,
            modifier = Modifier.weight(1f),
            color = MyAppTheme.colors.redText,
            style = MyAppTheme.typography.Semibold40,
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun DeleteAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: Int,
    dialogText: Int
) {
    AlertDialog(
        title = {
            Text(
                text = stringResource(id = dialogTitle),
                style = MyAppTheme.typography.Semibold57,
                color = MyAppTheme.colors.black
            )
        },
        text = {
            Text(
                text = stringResource(id = dialogText),
                style = MyAppTheme.typography.Regular46,
                color = MyAppTheme.colors.gray2
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(stringResource(id = R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(stringResource(id = R.string.dismiss))
            }
        }
    )
}

@Composable
fun NoDataMessage(
    title: String,
    details: String,
    painterRes: Int = R.drawable.receipt_long_off,
    iconSize: Dp = 50.dp,
    titleTextStyle: TextStyle = MyAppTheme.typography.Regular51,
    detailsTextStyle: TextStyle = MyAppTheme.typography.Regular44,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    Box(
        modifier = modifier
            .background(MyAppTheme.colors.transparent),
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
                tint = MyAppTheme.colors.gray2,
                modifier = Modifier.size(iconSize)
            )

            Text(
                text = title,
                style = titleTextStyle,
                color = MyAppTheme.colors.gray2,
                textAlign = TextAlign.Center
            )
            Text(
                text = details,
                style = detailsTextStyle,
                color = MyAppTheme.colors.gray3,
                textAlign = TextAlign.Center
            )

        }
    }
}

@Composable
fun AccountItem(
    isSelected: Boolean,
    isEditMode: Boolean,
    isEditable: Boolean,
    name: String,
    onSelect: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    @DrawableRes symbolId: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(40.dp)
            .fillMaxWidth()
            .roundedCornerBackground(MyAppTheme.colors.transparent)
            .clickable(enabled = isEditMode) { onSelect() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.item_padding))
    ) {
        if (isEditMode)
            RadioButton(selected = isSelected, onClick = onSelect)
        Icon(
            painter = painterResource(symbolId),
            contentDescription = "bank",
            tint = MyAppTheme.colors.lightBlue1,
            modifier = Modifier.size(dimensionResource(id = R.dimen.small_icon_size))
        )
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.item_padding)))
        Text(
            text = name,
            style = MyAppTheme.typography.Semibold52_5,
            color = MyAppTheme.colors.black
        )

        if (isEditMode && isEditable) {
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "edit",
                modifier = Modifier
                    .roundedCornerBackground(MyAppTheme.colors.transparent)
                    .clickable { onEditClick() }
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.item_padding)))

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "delete",
                modifier = Modifier
                    .roundedCornerBackground(MyAppTheme.colors.transparent)
                    .clickable { onDeleteClick() }
            )
        }
    }
}

@Composable
fun AccountTypeItem(
    @StringRes titleId: Int,
    selectPaymentId: Long,
    editAnimPaymentId: Long = 0L,
    editAnimRun: Boolean = false,
    isEditMode: Boolean = false,
    isEditable: Boolean = false,
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
            .padding(horizontal = dimensionResource(id = R.dimen.padding))
    ) {
        val scope = rememberCoroutineScope()
        val baseColor = MyAppTheme.colors.itemBg
        val targetAnimColor = MyAppTheme.colors.lightBlue1

        AccountHeadingItem(titleId)
        Column(
            modifier = modifier
                .fillMaxWidth()
                .roundedCornerBackground(MyAppTheme.colors.itemBg)
                /*.background(
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
                    color = MyAppTheme.colors.itemBg
                )*/
                .padding(dimensionResource(id = R.dimen.padding))
        ) {

            dataList.forEach() { item ->

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
                            targetValue = baseColor,
                            animationSpec = tween(Util.EDIT_ITEM_ANIM_TIME)
                        )
                    }
                    modifier.background(itemAnimateColor.value)
                } else {
                    Modifier
                }

                val id = when (item.modeName) {
                    "Bank" -> {
                        R.drawable.ic_bank
                    }

                    "Cash" -> {
                        R.drawable.ic_cash
                    }

                    "Card" -> {
                        R.drawable.ic_card
                    }

                    "Cheque" -> {
                        R.drawable.ic_cheque
                    }

                    "Net-banking" -> {
                        R.drawable.ic_net_banking
                    }

                    "Upi" -> {
                        R.drawable.ic_upi
                    }

                    else -> {
                        R.drawable.ic_payment
                    }
                }
                AccountItem(
                    isSelected = item.id == selectPaymentId,
                    isEditMode = isEditMode,
                    name = item.name,
                    symbolId = id,
                    isEditable = (item.preAdded == 0 && isEditable),
                    onSelect = { onSelect(item) },
                    onDeleteClick = { onDeleteClick(item) },
                    onEditClick = { onEditClick(item.id) },
                    modifier = modifierColor
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
        UserProfile()
    }
}

@Preview(showBackground = true)
@Composable
private fun DialogTextFieldItemPreview() {
    PennyPalTheme(darkTheme = true) {
        DialogTextFieldItem(
            imageVector = Icons.Default.PersonOutline,
            placeholder = R.string.amount_placeholder
        )
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
        this,
        message,
        Toast.LENGTH_SHORT
    ).show()
}

