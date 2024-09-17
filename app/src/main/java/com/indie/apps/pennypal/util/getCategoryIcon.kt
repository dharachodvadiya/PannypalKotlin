package com.indie.apps.pennypal.util

import com.indie.apps.pennypal.R

internal fun getCategoryIcon(categoryName: String) : Int {
    return when(categoryName){
        "Other" -> R.drawable.ic_more
        "Bills and Utilities" -> R.drawable.ic_bill
        "Education" -> R.drawable.ic_education
        "Entertainment" -> R.drawable.ic_games
        "Food and Dining" -> R.drawable.ic_food
        "Gift and Donation" -> R.drawable.ic_gift
        "Insurance" -> R.drawable.ic_insurance
        "Investments" -> R.drawable.ic_investment
        "Medical" -> R.drawable.ic_medical
        "Personal Care" -> R.drawable.ic_personal_care
        "Rent" -> R.drawable.ic_rent
        "Shopping" -> R.drawable.ic_shopping
        "Taxes" -> R.drawable.ic_tax
        "Travelling" -> R.drawable.ic_traveling
        "Salary" -> R.drawable.ic_salary
        "Rewards" -> R.drawable.ic_rewared
        else -> R.drawable.ic_personal_care
    }
}