package com.indie.apps.pennypal.data.module

import androidx.room.PrimaryKey

data class MerchantNameAndDetails(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val details: String? = null
)
