package com.indie.apps.pennypal.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme

@Composable
internal fun getColorFromId(id: Int): Color {
    val index = id % 16
    return when (index) {
        0 -> MyAppTheme.colors.categoryOther
        1 -> MyAppTheme.colors.categoryBills
        2 -> MyAppTheme.colors.categoryEducation
        3 -> MyAppTheme.colors.categoryEntertainment
        4 -> MyAppTheme.colors.categoryFood
        5 -> MyAppTheme.colors.categoryGift
        6 -> MyAppTheme.colors.categoryInsurance
        7 -> MyAppTheme.colors.categoryInvestment
        8 -> MyAppTheme.colors.categoryMedical
        9 -> MyAppTheme.colors.categoryPersonalCare
        10 -> MyAppTheme.colors.categoryRent
        11 -> MyAppTheme.colors.categoryShopping
        12 -> MyAppTheme.colors.categoryTax
        13 -> MyAppTheme.colors.categoryTravelling
        14 -> MyAppTheme.colors.categorySalary
        15 -> MyAppTheme.colors.categoryRewards
        else -> MyAppTheme.colors.categoryOther
    }
}