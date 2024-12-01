package com.indie.apps.pennypal.presentation.ui.screen.budget

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIdList
import com.indie.apps.pennypal.presentation.ui.component.NoDataMessage
import com.indie.apps.pennypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomProgressItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme

@Composable
fun BudgetTopBar(
    title: String = "",
    onNavigationUp: () -> Unit,
    onUpcomingBudgetClick: () -> Unit,
    onPastBudgetClick: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    TopBarWithTitle(
        title = title, onNavigationUp = {
            onNavigationUp()
        },
        trailingContent = {

            Column {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More Options")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            onUpcomingBudgetClick()
                        },
                        text = { CustomText(text = stringResource(id = R.string.upcoming_boudget)) })

                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            onPastBudgetClick()
                        },
                        text = { CustomText(text = stringResource(id = R.string.past_boudget)) })
                }
            }
        },
        contentAlignment = Alignment.Center,
        modifier = modifier
    )
}

@Composable
fun BudgetGroupItem(
    title: Int,
    budgetList: List<BudgetWithSpentAndCategoryIdList>,
    onBudgetItemClick: (Long) -> Unit,
    noDataTitleId: Int,
    noDataDetailId: Int,
    btnTextId: Int,
    onAddClick: () -> Unit,
    isShowAddButton: Boolean = false,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .roundedCornerBackground(MyAppTheme.colors.itemBg)
            .padding(dimensionResource(id = R.dimen.item_inner_padding))
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomText(
                text = stringResource(id = title),
                style = MyAppTheme.typography.Regular51,
                color = MyAppTheme.colors.gray1
            )

            if (isShowAddButton && budgetList.isNotEmpty()) {

                Spacer(modifier = Modifier.weight(1f))
                PrimaryButton(
                    onClick = onAddClick
                ) {
                    CustomText(
                        text = stringResource(id = R.string.set_up_budget),
                        style = MyAppTheme.typography.Medium40,
                        color = MyAppTheme.colors.gray0,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

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

        if (budgetList.isEmpty()) {

            NoDataMessage(
                title = stringResource(id = noDataTitleId),
                details = stringResource(id = noDataDetailId),
                iconSize = 0.dp,
                titleColor = MyAppTheme.colors.gray0,
                detailsColor = MyAppTheme.colors.gray2,
                titleTextStyle = MyAppTheme.typography.Medium54,
                isClickable = false,
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(id = R.dimen.item_inner_padding))
            )

            PrimaryButton(onClick = onAddClick) {
                CustomText(
                    text = stringResource(id = btnTextId),
                    style = MyAppTheme.typography.Regular44,
                    color = MyAppTheme.colors.gray0,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

    }
}
