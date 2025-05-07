package com.indie.apps.pennypal.util.app_enum

enum class AnalysisPeriod(val id: Int) {
    MONTH(1),
    YEAR(2),
}

fun AnalysisPeriod.toShowDataPeriod(): ShowDataPeriod {
    return when (this) {
        AnalysisPeriod.MONTH -> ShowDataPeriod.THIS_MONTH
        AnalysisPeriod.YEAR -> ShowDataPeriod.THIS_YEAR
    }
}