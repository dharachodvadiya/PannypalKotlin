package com.indie.apps.pennypal.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme

@Composable
internal fun GetCategoryColor(categoryName: String): Color {
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