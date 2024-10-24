package com.indie.apps.pennypal.presentation.ui.screen.budget

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme

@Composable
fun BudgetTopBar(
    title: String = "",
    onNavigationUp: () -> Unit,
    onAddClick: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    TopBarWithTitle(
        title = title,
        onNavigationUp = {
            onNavigationUp()
        },
        contentAlignment = Alignment.Center,
        trailingContent = {
            PrimaryButton(
                bgColor = MyAppTheme.colors.white,
                borderStroke = BorderStroke(
                    width = 1.dp,
                    color = MyAppTheme.colors.gray1
                ),
                onClick = onAddClick,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.top_bar_profile))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MyAppTheme.colors.gray1
                )
            }
        },
        modifier = modifier
    )
}