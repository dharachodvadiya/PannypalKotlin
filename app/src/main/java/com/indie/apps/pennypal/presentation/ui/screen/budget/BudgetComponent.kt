package com.indie.apps.pennypal.presentation.ui.screen.budget

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIdList
import com.indie.apps.pennypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomProgressItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.ListItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Util

@Composable
fun BudgetTopBar(
    title: String = "",
    onNavigationUp: () -> Unit,
    onAddClick: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    TopBarWithTitle(title = title, onNavigationUp = {
        onNavigationUp()
    }, contentAlignment = Alignment.Center, trailingContent = {
        PrimaryButton(
            bgColor = MyAppTheme.colors.white,
            borderStroke = BorderStroke(
                width = 1.dp, color = MyAppTheme.colors.gray1
            ),
            onClick = onAddClick,
            modifier = Modifier.size(dimensionResource(R.dimen.top_bar_profile))
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = MyAppTheme.colors.gray1
            )
        }
    }, modifier = modifier
    )
}

@Composable
fun BudgetGroupItem(
    title: Int,
    budgetList: List<BudgetWithSpentAndCategoryIdList>,
    onBudgetItemClick : (Long) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .roundedCornerBackground(MyAppTheme.colors.itemBg)
            .padding(dimensionResource(id = R.dimen.item_inner_padding))
    ) {
        CustomText(
            text = stringResource(id = title),
            style = MyAppTheme.typography.Regular51,
            color = MyAppTheme.colors.gray1
        )
        budgetList.forEach { item ->
            CustomProgressItem(
                name = item.title,
                totalAmount = item.budgetAmount,
                spentAmount = item.spentAmount,
                onClick = {
                    onBudgetItemClick(item.id)
                }
            )
        }

    }
}
