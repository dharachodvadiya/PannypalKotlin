package com.indie.apps.pannypal.presentation.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.TopBar
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun MyAppTopBar(
    title: String,
    onNavigationUp: () -> Unit,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.CenterStart,
    bgColor: Color = MyAppTheme.colors.white
) {
    TopBar(
        isBackEnable = true,
        onBackClick = onNavigationUp,
        content = {
            Text(
                text = title,
                style = MyAppTheme.typography.Semibold57,
                color = MyAppTheme.colors.black
            )
        },
        modifier = modifier,
        contentAlignment = contentAlignment,
        bgColor = bgColor
    )
}

@Composable
fun UserProfile(
    borderWidth : Float = 0f,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeHolder: String = "Placeholder",
    textStyle: TextStyle = MyAppTheme.typography.Medium56,
    placeHolderTextStyle: TextStyle = MyAppTheme.typography.Regular51,
    readOnly: Boolean = false,
    trailingIcon: @Composable() (() -> Unit)? = null,
    imeAction: ImeAction = ImeAction.Default,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    bgColor: Color = MyAppTheme.colors.white,
    onDoneAction: (() -> Unit)? = null,
    onNextAction: (() -> Unit)? = null,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(R.dimen.round_corner)))
            .background(bgColor)
            .padding(vertical = 5.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        val colors = TextFieldDefaults.colors(
            cursorColor = MyAppTheme.colors.brand,
            focusedIndicatorColor = MyAppTheme.colors.transparent,
            unfocusedIndicatorColor = MyAppTheme.colors.transparent,
            disabledIndicatorColor = MyAppTheme.colors.transparent,
            focusedContainerColor = MyAppTheme.colors.transparent,
            unfocusedContainerColor = MyAppTheme.colors.transparent
        )

        BasicTextField(
            readOnly = readOnly,
            value = value,
            modifier = textModifier
                .fillMaxWidth(),
            onValueChange = onValueChange,
            textStyle = textStyle,
            keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = keyboardType),
            keyboardActions = KeyboardActions(onDone = {onDoneAction}, onNext = {onNextAction}),
            singleLine = true,
            decorationBox = @Composable { innerTextField ->
                // places leading icon, text field with label and placeholder, trailing icon
                TextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    placeholder = {
                        Text(
                            text = placeHolder,
                            style = placeHolderTextStyle,
                            color = MyAppTheme.colors.gray2
                        )
                    },
                    trailingIcon = trailingIcon,
                    enabled = true,
                    interactionSource = remember { MutableInteractionSource()},
                    visualTransformation = VisualTransformation.None,
                    singleLine = true,
                    colors = colors,
                    contentPadding = paddingValues
                )
            }
        )
    }


}

@Preview()
@Composable
private fun MyAppTopBarPreview() {
    PannyPalTheme {
        MyAppTopBar(
            title = "title",
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

@Preview()
@Composable
private fun MyAppTextFieldPreview() {
    PannyPalTheme {
        MyAppTextField(
            value = "",
            onValueChange = {}
        )
    }
}