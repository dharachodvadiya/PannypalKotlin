package com.indie.apps.pannypal.presentation.ui.component.custom.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.component.screen.TopBarProfile
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun TopBar(
    onBackClick: () -> Unit = {},
    leadingContent: @Composable() (() -> Unit)? = null,
    content: @Composable() (() -> Unit)? = null,
    trailingContent: @Composable() (() -> Unit)? = null,
    modifier: Modifier = Modifier.background(MyAppTheme.colors.transparent),
    isBackEnable: Boolean = true,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.top_bar))
            .padding(horizontal = dimensionResource(R.dimen.padding)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding))
    ) {
        if(isBackEnable)
        {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.clickable {
                    onBackClick()
                })
        }

        if(leadingContent != null)
        {
            leadingContent()
        }
        Row(modifier = Modifier.weight(1f))
        {
            if(content != null)
                content()
        }

        if(trailingContent != null)
        {
            trailingContent()
        }

    }
}

@Preview()
@Composable
private fun SearchviewPreview() {
    PannyPalTheme {
        TopBar(
            isBackEnable = true,
            leadingContent = { TopBarProfile({}) },
            trailingContent = {
                IconButton(onClick = {
                }) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Profile",
                        tint = MyAppTheme.colors.black
                    )
                }
            }
        )
    }
}