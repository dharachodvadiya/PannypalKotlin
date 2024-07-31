package com.indie.apps.pannypal.presentation.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.MyAppTextField
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.TopBar
import com.indie.apps.pannypal.presentation.ui.state.TextFieldState
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun TopBarWithTitle(
    isBackEnable: Boolean = true,
    title: String,
    titleStyle: TextStyle = MyAppTheme.typography.Semibold57,
    onNavigationUp: () -> Unit,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.CenterStart,
    bgColor: Color = MyAppTheme.colors.white,
    trailingContent: @Composable() (() -> Unit)? = null
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
    val iconGradient = MyAppTheme.colors.gradientBlue
    val borderModifier = if (borderWidth > 0) {
        Modifier
            .border(
                BorderStroke(
                    width = borderWidth.dp,
                    color = MyAppTheme.colors.gray0
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
                color = MyAppTheme.colors.gray0,
                offsetX = (4).dp,
                offsetY = (5).dp,
                blurRadius = 5.dp,
            )
            .background(
                color = MyAppTheme.colors.white,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "User",
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.user_image_size))
                .graphicsLayer(alpha = 0.99f)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(linearGradientsBrush(iconGradient), blendMode = BlendMode.SrcAtop)
                    }
                }
        )
    }
}

@Composable
fun BottomSaveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PrimaryButton(
        bgBrush = linearGradientsBrush(MyAppTheme.colors.gradientBlue),
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.button_height)),
        onClick = onClick,
    ) {
        Text(
            text = stringResource(id = R.string.save),
            style = MyAppTheme.typography.Bold49_5,
            color = MyAppTheme.colors.white,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun DialogTextFieldItem(
    imageVector: ImageVector,
    textState: TextFieldState = TextFieldState(),
    placeholder: Int,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier,
    textLeadingContent: @Composable() (() -> Unit)? = null
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
                .height(dimensionResource(id = R.dimen.new_entry_field_hight))
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
                    textState.text = it },
                placeHolder = stringResource(placeholder),
                textStyle = MyAppTheme.typography.Medium46,
                keyboardType = keyboardType,
                textLeadingContent = textLeadingContent,
                placeHolderTextStyle = MyAppTheme.typography.Regular46,
                modifier = Modifier.height(dimensionResource(id = R.dimen.new_entry_field_hight)),
                bgColor = MyAppTheme.colors.gray0,
                paddingValues = PaddingValues(horizontal = dimensionResource(id = R.dimen.item_content_padding))
            )
        }

        Box(modifier = Modifier
            .height(20.dp)
            .fillMaxWidth())
        {
            TextFieldError(
                textError = textState.getError())
        }
    }



}

@Composable
internal fun TextFieldError(textError: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {

        Text(
            text = textError,
            modifier = Modifier.weight(1f),
            color = MyAppTheme.colors.redText,
            style = MyAppTheme.typography.Semibold40,
            textAlign = TextAlign.End
        )
    }
}


@Preview()
@Composable
private fun MyAppTopBarPreview() {
    PannyPalTheme {
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
    PannyPalTheme {
        UserProfile()
    }
}

@Preview(showBackground = true)
@Composable
private fun DialogTextFieldItemPreview() {
    PannyPalTheme {
        DialogTextFieldItem(
            imageVector = Icons.Default.PersonOutline,
            placeholder = R.string.amount_placeholder
        )
    }
}

