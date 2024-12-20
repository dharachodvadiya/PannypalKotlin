package com.indie.apps.pennypal.presentation.ui.component.custom.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun TopBar(
    onBackClick: () -> Unit = {},
    leadingContent: @Composable (() -> Unit)? = null,
    content: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    isBackEnable: Boolean = true,
    contentAlignment: Alignment = Alignment.CenterStart,
    bgColor: Color = MyAppTheme.colors.transparent
) {
    val topStatusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(bgColor)
            .padding(top = topStatusBarPadding)
            .height(dimensionResource(R.dimen.top_bar))
            .padding(horizontal = dimensionResource(R.dimen.padding)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding))
    ) {
        var rightPadding = 0.dp
        if (isBackEnable) {
            rightPadding =
                if (contentAlignment == Alignment.Center) (24.dp + dimensionResource(id = R.dimen.padding)) else 0.dp
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .roundedCornerBackground(MyAppTheme.colors.transparent)
                    .clickableWithNoRipple {
                        onBackClick()
                    })
        }

        if (leadingContent != null) {
            leadingContent()
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(start = 0.dp, top = 0.dp, bottom = 0.dp, end = rightPadding),
            contentAlignment = contentAlignment
        )
        {
            if (content != null)
                content()
        }

        if (trailingContent != null) {
            trailingContent()
        }

    }

    /* Box(
         modifier = modifier
             .fillMaxWidth()
             .background(bgColor)
             .height(dimensionResource(R.dimen.top_bar))
             .padding(horizontal = dimensionResource(R.dimen.padding)),
         contentAlignment = contentAlignment
     )
     {
         if (content != null)
             content()

         Row(
             verticalAlignment = Alignment.CenterVertically,
             modifier = modifier
                 .matchParentSize(),
             horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding))
         ){
             if (isBackEnable) {
                 Icon(
                     Icons.AutoMirrored.Filled.ArrowBack,
                     contentDescription = "Back",
                     modifier = Modifier.clickable {
                         onBackClick()
                     })
             }

             if (leadingContent != null) {
                 leadingContent()
             }

             Spacer(modifier = Modifier.weight(1f))

             if (trailingContent != null) {
                 trailingContent()
             }
         }
     }*/
}

@Preview
@Composable
private fun TopBarPreview() {
    PennyPalTheme(darkTheme = true) {
        TopBar(
            isBackEnable = true,
            leadingContent = { }
        )
    }
}
