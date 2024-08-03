package com.indie.apps.pannypal.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "merchant_data",
    foreignKeys = [
        ForeignKey(
            entity = Merchant::class,
            parentColumns = ["id"],
            childColumns = ["merchant_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Payment::class,
            parentColumns = ["id"],
            childColumns = ["payment_id"],
            onDelete = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index(value = ["merchant_id"]),
        Index(value = ["payment_id"])
    ]
)
data class MerchantData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "merchant_id")
    val merchantId: Long,

    @ColumnInfo(name = "payment_id")
    val paymentId: Long,

    @ColumnInfo(name = "date_milli")
    val dateInMilli: Long,

    val details: String? = null,

    val amount: Double,

    val type: Int, // -1 for expense, 1 for income


    )
