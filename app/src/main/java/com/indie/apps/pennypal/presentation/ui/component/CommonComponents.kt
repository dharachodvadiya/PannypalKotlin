package com.indie.apps.pennypal.presentation.ui.component

import android.content.Context
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.MyAppTextField
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.SearchView
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.TopBar
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun TopBarWithTitle(
    isBackEnable: Boolean = true,
    title: String,
    titleStyle: TextStyle = MyAppTheme.typography.Semibold57,
    onNavigationUp: () -> Unit,
    modifier: Modifier = Modifier,
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
    modifier: Modifier = Modifier
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
    @StringRes textId : Int = R.string.save,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
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
    modifier: Modifier = Modifier
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
    modifier: Modifier = Modifier,
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
                .background(
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
                    color = MyAppTheme.colors.transparent
                ),
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
    onSelect: ()->Unit,
    onEditClick: ()-> Unit,
    onDeleteClick: ()-> Unit,
    @DrawableRes symbolId: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(40.dp),
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
                modifier = Modifier.clickable { onEditClick() }
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.item_padding)))

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "delete",
                modifier = Modifier.clickable { onDeleteClick() }
            )
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

