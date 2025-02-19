package com.indie.apps.pennypal.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme

@Composable
internal fun getCategoryColor(categoryName: String): Color {
    return when (categoryName) {
        "Other" -> MyAppTheme.colors.categoryOther
        "Bills and Utilities" -> MyAppTheme.colors.categoryBills
        "Education" -> MyAppTheme.colors.categoryEducation
        "Entertainment" -> MyAppTheme.colors.categoryEntertainment
        "Food and Dining" -> MyAppTheme.colors.categoryFood
        "Gift and Donation" -> MyAppTheme.colors.categoryGift
        "Insurance" -> MyAppTheme.colors.categoryInsurance
        "Investments" -> MyAppTheme.colors.categoryInvestment
        "Medical" -> MyAppTheme.colors.categoryMedical
        "Personal Care" -> MyAppTheme.colors.categoryPersonalCare
        "Rent" -> MyAppTheme.colors.categoryRent
        "Shopping" -> MyAppTheme.colors.categoryShopping
        "Taxes" -> MyAppTheme.colors.categoryTax
        "Travelling" -> MyAppTheme.colors.categoryTravelling
        "Salary" -> MyAppTheme.colors.categorySalary
        "Rewards" -> MyAppTheme.colors.categoryRewards
        else -> MyAppTheme.colors.categoryOther
    }
}

@Composable
internal fun getCategoryColorById(categoryId: Int): Color {
    return when (categoryId) {
        1 -> MyAppTheme.colors.categoryOther
        2 -> MyAppTheme.colors.categoryBills
        3 -> MyAppTheme.colors.categoryEducation
        4 -> MyAppTheme.colors.categoryEntertainment
        5 -> MyAppTheme.colors.categoryFood
        6 -> MyAppTheme.colors.categoryGift
        7 -> MyAppTheme.colors.categoryInsurance
        8 -> MyAppTheme.colors.categoryInvestment
        9 -> MyAppTheme.colors.categoryMedical
        10 -> MyAppTheme.colors.categoryPersonalCare
        11 -> MyAppTheme.colors.categoryRent
        12 -> MyAppTheme.colors.categoryShopping
        13 -> MyAppTheme.colors.categoryTax
        14 -> MyAppTheme.colors.categoryTravelling
        15 -> MyAppTheme.colors.categorySalary
        16 -> MyAppTheme.colors.categoryRewards
        else -> MyAppTheme.colors.categoryOther
    }
}

