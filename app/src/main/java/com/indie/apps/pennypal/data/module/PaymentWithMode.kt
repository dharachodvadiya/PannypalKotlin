package com.indie.apps.pennypal.data.module

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

data class PaymentWithMode(
    val id: Long = 0,

    val name: String,

    val modeId: Long = 0,

    val preAdded: Int = 0,

    val modeName: String
)
