package com.indie.apps.pennypal.presentation.ui.screen.add_budget

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.enum.PeriodType
import com.indie.apps.pennypal.data.module.TabItemInfo
import com.indie.apps.pennypal.data.module.category.CategoryAmount
import com.indie.apps.pennypal.presentation.ui.component.DialogSelectableItem
import com.indie.apps.pennypal.presentation.ui.component.DialogTextFieldItem
import com.indie.apps.pennypal.presentation.ui.component.TextFieldError
import com.indie.apps.pennypal.presentation.ui.component.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomTab
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.ListItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.MyAppTextField
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.getCategoryColorById
import com.indie.apps.pennypal.util.getCategoryIconById
import kotlin.enums.EnumEntries

@Composable
fun AddBudgetTopSelectionButton(
    list: EnumEntries<PeriodType>,
    selectBudgetPeriod: PeriodType,
    onSelect: (PeriodType) -> Unit,
    modifier: Modifier = Modifier,
) {

    val tabItems = list.map { period ->
        TabItemInfo(
            title = when (period) {
                PeriodType.MONTH -> R.string.month
                PeriodType.YEAR -> R.string.year
                PeriodType.ONE_TIME -> R.string.one_time
            },
            selectBgColor = MyAppTheme.colors.itemSelectedBg,
            unSelectBgColor = MyAppTheme.colors.itemBg,
            selectContentColor = MyAppTheme.colors.black,
            unSelectContentColor = MyAppTheme.colors.gray1
        )
    }

    Row(
        modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        CustomTab(tabList = tabItems,
            selectedIndex = list.indexOf(selectBudgetPeriod),
            onTabSelected = {
                onSelect(list[it])
            })

    }

}

@Composable
fun AddBudgetFieldItem(
    categoryList: List<CategoryAmount>,
    isExpanded: Boolean,
    totalCategoryAmount: Double,
    remainingCategoryAmount: Double,
    onCategoryExpandChange: (Boolean) -> Unit,
    onCategoryAmountChange: (Long, String) -> Unit,
    onSelectCategory: () -> Unit,
    selectBudgetPeriod: PeriodType,
    currentYear: String?,
    currentMonth: String?,
    currentFromDate: String?,
    periodErrorText: String,
    periodFromErrorText: String,
    periodToErrorText: String,
    categoryErrorText: String,
    categoryBudgetErrorText: String,
    currentToDate: String?,
    onSelectYear: () -> Unit,
    onSelectMonth: () -> Unit,
    onSelectFromDate: () -> Unit,
    onSelectToDate: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    amount: TextFieldState,
    onAmountTextChange: (String) -> Unit,
    budgetTitle: TextFieldState,
    onBudgetTitleTextChange: (String) -> Unit,
    focusRequesterAmount: FocusRequester,
    focusRequesterTitle: FocusRequester,
    currency: String,
) {
    Column(
        modifier = modifier.background(MyAppTheme.colors.transparent),
    ) {
        DialogTextFieldItem(
            textState = budgetTitle,
            placeholder = R.string.budget_title_placeholder,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Title,
                    contentDescription = "title",
                    tint = MyAppTheme.colors.gray1
                )
            },
            onTextChange = onBudgetTitleTextChange,
            bgColor = MyAppTheme.colors.transparent,
            isBottomLineEnable = true,
            focusRequester = focusRequesterTitle,
            nextFocusRequester = focusRequesterAmount
        )

        DialogTextFieldItem(
            textState = amount,
            placeholder = R.string.amount_placeholder,
            leadingIcon = {
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CustomText(
                        text = currency,
                        color = MyAppTheme.colors.gray1,
                        style = MyAppTheme.typography.Regular66_5
                    )
                }
            },
            keyboardType = KeyboardType.Number,
            onTextChange = onAmountTextChange,
            bgColor = MyAppTheme.colors.transparent,
            isBottomLineEnable = true,
            focusRequester = focusRequesterAmount
        )

        AddBudgetPeriodFieldItem(
            selectBudgetPeriod = selectBudgetPeriod,
            currentYear = currentYear,
            currentMonth = currentMonth,
            currentFromDate = currentFromDate,
            currentToDate = currentToDate,
            onSelectYear = onSelectYear,
            onSelectMonth = onSelectMonth,
            onSelectFromDate = onSelectFromDate,
            onSelectToDate = onSelectToDate,
            periodErrorText = periodErrorText,
            periodFromErrorText = periodFromErrorText,
            periodToErrorText = periodToErrorText
        )



        DialogSelectableItem(
            text = if (categoryList.isNotEmpty()) "${categoryList.size} ${stringResource(id = R.string.category_selected)}" else "",
            label = R.string.select_category,
            onClick = { onSelectCategory() },
            placeholder = R.string.select_category,
            trailingContent = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Add",
                    tint = MyAppTheme.colors.gray1
                )
            },
            leadingContent = {
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "category",
                    tint = MyAppTheme.colors.gray1
                )
            },
            errorText = categoryErrorText
        )

        AddBudgetCategoryListItem(
            categoryList = categoryList,
            totalAmount = totalCategoryAmount,
            remainingAmount = remainingCategoryAmount,
            isExpanded = isExpanded,
            onClick = onCategoryExpandChange,
            onAmountChange = onCategoryAmountChange,
            errorText = categoryBudgetErrorText,
            currency = currency
        )
        TextFieldError(
            textError = if (!isExpanded) categoryBudgetErrorText else ""
        )
    }
}

@Composable
fun AddBudgetPeriodFieldItem(
    selectBudgetPeriod: PeriodType,
    currentYear: String?,
    periodErrorText: String,
    periodFromErrorText: String,
    periodToErrorText: String,
    currentMonth: String?,
    currentFromDate: String?,
    currentToDate: String?,
    onSelectYear: () -> Unit,
    onSelectMonth: () -> Unit,
    onSelectFromDate: () -> Unit,
    onSelectToDate: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        if (selectBudgetPeriod == PeriodType.ONE_TIME) {
            DialogSelectableItem(
                label = R.string.period,
                text = currentFromDate,
                onClick = onSelectFromDate,
                placeholder = R.string.from,
                leadingContent = {
                    Icon(imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "calender",
                        tint = MyAppTheme.colors.gray1,
                        modifier = Modifier
                            .roundedCornerBackground(MyAppTheme.colors.transparent)
                            .clickableWithNoRipple {
                                onSelectFromDate()
                            })
                },
                errorText = periodFromErrorText,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding)))

            DialogSelectableItem(
                label = R.string.empty,
                onClick = onSelectToDate,
                text = currentToDate,
                placeholder = R.string.to,
                leadingContent = {
                    Icon(imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "calender",
                        tint = MyAppTheme.colors.gray1,
                        modifier = Modifier
                            .roundedCornerBackground(MyAppTheme.colors.transparent)
                            .clickableWithNoRipple {
                                onSelectToDate()
                            })
                },
                errorText = periodToErrorText,
                modifier = Modifier.weight(1f)
            )
        } else {
            DialogSelectableItem(
                label = R.string.period,
                text = if (selectBudgetPeriod == PeriodType.MONTH) currentMonth else currentYear,
                onClick = {
                    if (selectBudgetPeriod == PeriodType.MONTH) onSelectMonth()
                    else onSelectYear()
                },
                placeholder = if (selectBudgetPeriod == PeriodType.MONTH) R.string.select_month
                else R.string.select_year,
                leadingContent = {
                    Icon(imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "calender",
                        tint = MyAppTheme.colors.gray1,
                        modifier = Modifier
                            .roundedCornerBackground(MyAppTheme.colors.transparent)
                            .clickableWithNoRipple {
                                if (selectBudgetPeriod == PeriodType.MONTH) onSelectMonth()
                                else onSelectYear()
                            })
                },
                errorText = periodErrorText
            )
        }

    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun AddBudgetCategoryListItem(
    categoryList: List<CategoryAmount>,
    errorText: String = "",
    isExpanded: Boolean,
    totalAmount: Double,
    remainingAmount: Double,
    currency: String,
    onClick: (Boolean) -> Unit,
    onAmountChange: (Long, String) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .roundedCornerBackground(MyAppTheme.colors.itemBg)
            .padding(dimensionResource(id = R.dimen.padding))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .roundedCornerBackground(MyAppTheme.colors.transparent)
                .clickableWithNoRipple(
                    //    interactionSource = MutableInteractionSource(),
                    //    indication = null,
                ) { onClick(!isExpanded) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                CustomText(
                    text = stringResource(id = R.string.category_wise_limit),
                    style = MyAppTheme.typography.Medium54,
                    color = MyAppTheme.colors.black
                )
                Spacer(modifier = Modifier.height(5.dp))
                CustomText(
                    text = "${categoryList.size} " + stringResource(id = R.string.category_included_in_budget),
                    style = MyAppTheme.typography.Medium46,
                    color = MyAppTheme.colors.gray1
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "expandable",
                tint = MyAppTheme.colors.gray1,
                modifier = Modifier
                    .rotate(if (isExpanded) -90f else 90f)
            )
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .roundedCornerBackground(MyAppTheme.colors.brand)
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.padding), vertical = 5.dp
                    ), verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    CustomText(
                        text = stringResource(id = R.string.total_budget),
                        style = MyAppTheme.typography.Regular46,
                        color = MyAppTheme.colors.gray1
                    )
                    CustomText(
                        text = Util.getFormattedStringWithSymbol(totalAmount, currency),
                        style = MyAppTheme.typography.Medium54,
                        color = MyAppTheme.colors.black
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    CustomText(
                        text = stringResource(id = R.string.remaining),
                        style = MyAppTheme.typography.Regular46,
                        color = MyAppTheme.colors.gray1
                    )
                    CustomText(
                        text = Util.getFormattedStringWithSymbol(remainingAmount, currency),
                        style = MyAppTheme.typography.Medium54,
                        color = if (remainingAmount < 0) MyAppTheme.colors.redText else MyAppTheme.colors.black,
                    )
                }
            }
            TextFieldError(
                textError = errorText
            )

            categoryList.forEach { item ->
                CategoryListItem(
                    item = item,
                    onTextChange = {
                        onAmountChange(item.id, it)
                    }, itemBgColor = MyAppTheme.colors.transparent
                )
            }
        }
    }
}

@Composable
private fun CategoryListItem(
    item: CategoryAmount,
    itemBgColor: Color,
    onTextChange: (String) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxWidth()
) {
    val imageVector =
        ImageVector.vectorResource(getCategoryIconById(item.iconId, LocalContext.current))
    val tintColor = getCategoryColorById(item.iconColorId)
    val textState = remember {
        mutableStateOf(TextFieldState().apply {
            if (item.amount != 0.0) updateText(
                Util.getFormattedString(
                    item.amount
                )
            )
        })
    }

    ListItem(isClickable = false, leadingIcon = {
        RoundImage(
            imageVector = imageVector,
            imageVectorSize = 20.dp,
            //brush = linearGradientsBrush(MyAppTheme.colors.gradientBlue),
            tint = tintColor,
            backGround = MyAppTheme.colors.lightBlue2,
            contentDescription = "item",
            modifier = Modifier.size(40.dp)
        )

    }, content = {
        CustomText(
            text = item.name,
            style = MyAppTheme.typography.Semibold52_5,
            color = MyAppTheme.colors.black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }, trailingContent = {
        MyAppTextField(
            value = textState.value.text,
            onValueChange = {
                textState.value.updateText(it)
                onTextChange(it)
            },
            bgColor = MyAppTheme.colors.brand,
            placeHolder = stringResource(id = R.string.no_limit),
            textStyle = MyAppTheme.typography.Regular44.copy(textAlign = TextAlign.Center),
            placeHolderTextStyle = MyAppTheme.typography.Regular44.copy(textAlign = TextAlign.Center),
            modifier = Modifier
                .width(100.dp)
                .padding(5.dp),
            maxLines = 1,
            keyboardType = KeyboardType.Number

        )

    }, modifier = modifier, itemBgColor = itemBgColor
    )
}