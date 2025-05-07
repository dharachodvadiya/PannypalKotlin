package com.indie.apps.pennypal.util.app_enum

enum class PeriodType(val id: Int) {
    MONTH(1),
    YEAR(2),
    ONE_TIME(3);

    companion object {
        fun fromId(id: Int): PeriodType? = entries.find { it.id == id }
    }
}