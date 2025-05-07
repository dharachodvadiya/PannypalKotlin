package com.indie.apps.pennypal.data.database.db_entity

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
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = BaseCurrency::class,
            parentColumns = ["id"],
            childColumns = ["base_currency_id"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = BaseCurrency::class,
            parentColumns = ["id"],
            childColumns = ["original_currency_id"],
            onDelete = ForeignKey.NO_ACTION
        ),
    ],
    indices = [
        Index(value = ["merchant_id"]),
        Index(value = ["payment_id"]),
        Index(value = ["category_id"]),
        Index(value = ["base_currency_id"]),
        Index(value = ["original_currency_id"])
    ]
)
data class MerchantData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "merchant_id")
    val merchantId: Long? = null,

    @ColumnInfo(name = "category_id")
    val categoryId: Long,

    @ColumnInfo(name = "payment_id")
    val paymentId: Long,

    @ColumnInfo(name = "date_milli")
    val dateInMilli: Long,

    val details: String? = null,

    val amount: Double, //amount in base currency / converted amount

    @ColumnInfo(name = "base_currency_id")
    val baseCurrencyId: Long,

    @ColumnInfo(name = "original_amount") // amount which user entered
    val originalAmount: Double,

    @ColumnInfo(name = "original_currency_id")
    val originalCurrencyId: Long,

    val type: Int, // -1 for expense, 1 for income


)
