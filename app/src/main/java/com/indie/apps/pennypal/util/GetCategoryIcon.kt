package com.indie.apps.pennypal.util

import android.annotation.SuppressLint
import android.content.Context
import com.indie.apps.pennypal.R

internal fun getCategoryIcon(categoryName: String): Int {
    return when (categoryName) {
        "Other" -> R.drawable.ic_category_1
        "Bills and Utilities" -> R.drawable.ic_category_2
        "Education" -> R.drawable.ic_category_3
        "Entertainment" -> R.drawable.ic_category_4
        "Food and Dining" -> R.drawable.ic_category_5
        "Gift and Donation" -> R.drawable.ic_category_6
        "Insurance" -> R.drawable.ic_category_7
        "Investments" -> R.drawable.ic_category_8
        "Medical" -> R.drawable.ic_category_9
        "Personal Care" -> R.drawable.ic_category_10
        "Rent" -> R.drawable.ic_category_11
        "Shopping" -> R.drawable.ic_category_12
        "Taxes" -> R.drawable.ic_category_13
        "Travelling" -> R.drawable.ic_category_14
        "Salary" -> R.drawable.ic_category_15
        "Rewards" -> R.drawable.ic_category_16
        else -> R.drawable.ic_category_1
    }
}

@SuppressLint("DiscouragedApi")
internal fun getCategoryIconById(categoryId: Int, context: Context): Int {
    // Construct the drawable name dynamically
    val drawableName =
        "ic_category_${categoryId}"  // Example: "ic_category_1", "ic_category_2", etc.

    // Get the drawable resource ID based on the name
    val drawableId = context.resources.getIdentifier(drawableName, "drawable", context.packageName)

    // Return the drawable resource ID or a default if not found
    return if (drawableId != 0) drawableId else R.drawable.ic_category_1  // Default to ic_1 if not found
}
