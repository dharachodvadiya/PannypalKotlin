package com.indie.apps.pennypal.presentation.ui.component.custom.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Util

@Composable
fun CustomProgressItem(
    name: String,
    totalAmount: Double,
    spentAmount: Double,
    onClick: () -> Unit = {},
    isClickable: Boolean = true,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    ListItem(
        isClickable = isClickable,
        onClick = onClick,
        content = {
            Column {

                val remainAmount = totalAmount - spentAmount

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomText(
                        text = name,
                        style = MyAppTheme.typography.Regular46,
                        color = MyAppTheme.colors.black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    CustomText(
                        text = Util.getFormattedStringWithSymbol(remainAmount),
                        style = MyAppTheme.typography.Regular46,
                        color = if (remainAmount < 0) MyAppTheme.colors.redBg else MyAppTheme.colors.black,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Right,
                        maxLines = 1,
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomText(
                        text = "${stringResource(R.string.budget)} : ${
                            Util.getFormattedStringWithSymbol(
                                totalAmount
                            )
                        }",
                        style = MyAppTheme.typography.Medium40,
                        color = MyAppTheme.colors.gray2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    CustomText(
                        text = stringResource(R.string.remaining),
                        style = MyAppTheme.typography.Medium40,
                        color = MyAppTheme.colors.gray2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Right,
                        maxLines = 1,
                    )
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.item_inner_padding)))

                // Progress Bar
                val totalBudgetAmount = totalAmount

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
                    color = if (remainAmount < 0) MyAppTheme.colors.redBg else MyAppTheme.colors.lightBlue1,
                    trackColor = MyAppTheme.colors.gray1.copy(0.2f)
                )
                if (remainAmount < 0) {

                    Spacer(modifier = Modifier.height(5.dp))
                    CustomText(
                        text = stringResource(R.string.exceeded_limit_message),
                        style = MyAppTheme.typography.Regular44,
                        color = MyAppTheme.colors.redBg.copy(alpha = 0.7f),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }

            }

        },
        trailingContent = {
            if(isClickable)
            {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "edit",
                    tint = MyAppTheme.colors.black,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }


        },
        modifier = modifier,
        itemBgColor = MyAppTheme.colors.transparent
    )
}

@Composable
fun CustomProgressItemWithDate(
    name: String,
    date: String,
    totalAmount: Double,
    spentAmount: Double,
    onClick: () -> Unit = {},
    isClickable: Boolean = true,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    ListItem(
        isClickable = isClickable,
        onClick = onClick,
        content = {
            Column {

                val remainAmount = totalAmount - spentAmount

                CustomText(
                    text = name,
                    style = MyAppTheme.typography.Regular46,
                    color = MyAppTheme.colors.black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomText(
                        text = date,
                        style = MyAppTheme.typography.Regular46,
                        color = MyAppTheme.colors.gray1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    CustomText(
                        text = Util.getFormattedStringWithSymbol(remainAmount),
                        style = MyAppTheme.typography.Regular46,
                        color = if (remainAmount < 0) MyAppTheme.colors.redBg else MyAppTheme.colors.black,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Right,
                        maxLines = 1,
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomText(
                        text = "${stringResource(R.string.budget)} : ${
                            Util.getFormattedStringWithSymbol(
                                totalAmount
                            )
                        }",
                        style = MyAppTheme.typography.Medium40,
                        color = MyAppTheme.colors.gray2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    CustomText(
                        text = stringResource(R.string.remaining),
                        style = MyAppTheme.typography.Medium40,
                        color = MyAppTheme.colors.gray2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Right,
                        maxLines = 1,
                    )
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.item_inner_padding)))

                // Progress Bar
                val totalBudgetAmount = totalAmount

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
                    color = if (remainAmount < 0) MyAppTheme.colors.redBg else MyAppTheme.colors.lightBlue1,
                    trackColor = MyAppTheme.colors.gray1.copy(0.2f)
                )
                if (remainAmount < 0) {

                    Spacer(modifier = Modifier.height(5.dp))
                    CustomText(
                        text = stringResource(R.string.exceeded_limit_message),
                        style = MyAppTheme.typography.Regular44,
                        color = MyAppTheme.colors.redBg.copy(alpha = 0.7f),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }

            }

        },
        trailingContent = {
            if(isClickable)
            {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "edit",
                    tint = MyAppTheme.colors.black,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }


        },
        modifier = modifier,
        itemBgColor = MyAppTheme.colors.transparent
    )
}

@Preview
@Composable
private fun CustomProgressItemPreview() {
    PennyPalTheme(darkTheme = true) {
        CustomProgressItem(
            name = "aaa",
            totalAmount = 100.0,
            spentAmount = 30.0,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun CustomProgressItemDatePreview() {
    PennyPalTheme(darkTheme = true) {
        CustomProgressItemWithDate(
            name = "aaa",
            totalAmount = 100.0,
            spentAmount = 30.0,
            onClick = {},
            date = "25 january 2024"
        )
    }
}