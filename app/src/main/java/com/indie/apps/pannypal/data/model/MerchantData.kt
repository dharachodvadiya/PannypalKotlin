package com.indie.apps.pannypal.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "merchant_data"
)
data class MerchantData(
    @PrimaryKey(autoGenerate = true)
    val id: Long= 0,

    @ColumnInfo(name = "merchant_id")
    val merchantId: Long,

    @ColumnInfo(name = "payment_id")
    val paymentId: Long,

    @ColumnInfo(name = "date_milli")
    val dateInMilli: Long,

    val details: String?,

    val amount: Long,


)
