package com.indie.apps.pannypal.data.module

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

data class MerchantNameAndDetails(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val details: String? = null
)
