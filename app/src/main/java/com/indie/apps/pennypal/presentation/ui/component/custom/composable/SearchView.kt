package com.indie.apps.pennypal.presentation.ui.component.custom.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.presentation.ui.component.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun SearchView(
    trailingIcon: ImageVector? = null,
    textState: TextFieldState = TextFieldState(),
    onTextChange: (String) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    bgColor: Color = MyAppTheme.colors.white,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    Box(
        modifier = modifier
            .roundedCornerBackground(bgColor)
        /*.clip(RoundedCornerShape(dimensionResource(R.dimen.round_corner)))
        .background(bgColor)*/
    ) {

        MyAppTextField(
            value = textState.text,
            onValueChange = onTextChange,
            /*onValueChange = {
                textState.text = it
                onTextChange(it)
            },*/
            placeHolder = stringResource(com.indie.apps.cpp.R.string.search),
            imeAction = ImeAction.Search,
            //keyboardActions = KeyboardActions(onSearch = { onSearchClick(text) }),
            trailingIcon = {
                if (textState.text.isNotEmpty()) {
                    Icon(
                        modifier = Modifier
                            .roundedCornerBackground(MyAppTheme.colors.transparent)
                            .clickableWithNoRipple {
                                if (textState.text.isNotEmpty()) {
                                    //textState.text = ""
                                    onTextChange("")
                                }
                            },
                        imageVector = Icons.Filled.Close,
                        contentDescription = "close"
                    )

                } else if (trailingIcon != null) {
                    Icon(
                        modifier = Modifier
                            .roundedCornerBackground(MyAppTheme.colors.transparent)
                            .clickableWithNoRipple {
                                if (textState.text.isNotEmpty()) {
                                    //textState.text = ""
                                    onTextChange("")
                                }
                            },
                        imageVector = trailingIcon,
                        contentDescription = "search"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            bgColor = MyAppTheme.colors.transparent,
            textStyle = MyAppTheme.typography.Medium46,
            paddingValues = paddingValues,
            placeHolderColor = MyAppTheme.colors.gray1
        )


    }
}

@Preview
@Composable
private fun SearchViewPreview() {
    PennyPalTheme(darkTheme = true) {
        SearchView(onTextChange = {})
    }
}