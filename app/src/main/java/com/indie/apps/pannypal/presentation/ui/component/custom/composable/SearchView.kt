package com.indie.apps.pannypal.presentation.ui.component.custom.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun SearchView(
    trailingIcon: ImageVector? = null,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var textState by remember { mutableStateOf("") }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(R.dimen.round_corner)))
    ) {
        TextField(
            value = textState,
            onValueChange = {
                textState = it
                onTextChange(textState)
            },
            colors = TextFieldDefaults.colors(
                cursorColor = MyAppTheme.colors.brand,
                focusedIndicatorColor = MyAppTheme.colors.transparent,
                unfocusedIndicatorColor = MyAppTheme.colors.transparent,
                disabledIndicatorColor = MyAppTheme.colors.transparent,
                focusedContainerColor = MyAppTheme.colors.transparent,
                unfocusedContainerColor = MyAppTheme.colors.transparent
            ),
            placeholder = {
                Text(
                    text = "Search",
                    style = MyAppTheme.typography.Regular57,
                    color = MyAppTheme.colors.gray2
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            //keyboardActions = KeyboardActions(onSearch = { onSearchClick(text) }),
            trailingIcon = {
                if (textState.isNotEmpty()) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (textState.isNotEmpty()) {
                                textState = ""
                            }
                        },
                        imageVector = Icons.Filled.Close,
                        contentDescription = "close"
                    )
                }else if(trailingIcon != null){
                    Icon(
                        modifier = Modifier.clickable {
                            if (textState.isNotEmpty()) {
                                textState = ""
                            }
                        },
                        imageVector = trailingIcon,
                        contentDescription = "search"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MyAppTheme.colors.white),
            textStyle = MyAppTheme.typography.Medium56
        )


    }
}

@Preview()
@Composable
private fun SearchviewPreview() {
    PannyPalTheme {
        SearchView(
            onTextChange = { }
        )
    }
}