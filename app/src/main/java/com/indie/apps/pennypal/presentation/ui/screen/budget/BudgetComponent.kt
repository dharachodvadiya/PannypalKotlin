package com.indie.apps.pennypal.presentation.ui.screen.budget

import android.annotation.SuppressLint
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
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIds
import com.indie.apps.pennypal.presentation.ui.component.TopBarWithTitle
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
            BudgetListItem(
                name = item.title,
                budgetAmount = item.budgetAmount,
                spentAmount = item.spentAmount
            )
        }

    }
}

@Composable
private fun BudgetListItem(
    name: String,
    budgetAmount: Double,
    spentAmount: Double,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    ListItem(
        content = {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {

                val remainAmount = budgetAmount-spentAmount
                CustomText(
                    text = name,
                    style = MyAppTheme.typography.Regular46,
                    color = MyAppTheme.colors.black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomText(
                        text = "Total : ${Util.getFormattedStringWithSymbol(budgetAmount)}",
                        style = MyAppTheme.typography.Regular46,
                        color = MyAppTheme.colors.gray1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    CustomText(
                        text = "Remain : ${Util.getFormattedStringWithSymbol(remainAmount)}",
                        style = MyAppTheme.typography.Regular46,
                        color = if(remainAmount <0) MyAppTheme.colors.redText else MyAppTheme.colors.gray1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Right,
                        maxLines = 1,
                    )
                }

                // Progress Bar
                val totalBudgetAmount = budgetAmount
                val spentAmount = spentAmount

                // Calculate the percentage of the budget spent
                val progress = if (totalBudgetAmount > 0) {
                    (spentAmount / totalBudgetAmount).coerceIn(0.0, 1.0)
                        .toFloat() // Ensure it stays within 0 to 1 range
                } else {
                    0.0F
                }

                // Display progress bar
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp))
                        .height(10.dp),
                    color = if(remainAmount<0) MyAppTheme.colors.redBg else MyAppTheme.colors.lightBlue1,
                    trackColor = MyAppTheme.colors.gray1
                )

            }

        },
        trailingContent = {


        },
        modifier = modifier,
        itemBgColor = MyAppTheme.colors.transparent
    )
}

@Preview
@Composable
private fun BudgetListItemPreview() {
    PennyPalTheme(darkTheme = true) {
        BudgetListItem(
            name = "aaa",
            budgetAmount = 100.0,
            spentAmount = 30.0
        )
    }
}