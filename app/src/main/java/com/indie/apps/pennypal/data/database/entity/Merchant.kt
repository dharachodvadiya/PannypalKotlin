package com.indie.apps.pennypal.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.indie.apps.pennypal.data.module.MerchantNameAndDetails

@Entity(
    tableName = "merchant",
    indices = [Index(value = ["name"], unique = true)]
)
data class Merchant(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val name: String,

    @ColumnInfo(name = "phone_num")
    val phoneNumber: String? = null,

    @ColumnInfo(name = "country_code")
    val countryCode: String? = null,

    //@ColumnInfo(name = "date_milli")
    //val dateInMilli: Long = 0,

    val details: String? = null,

    @ColumnInfo(name = "soft_delete")
    val softDeleted: Int = 0,

    //@ColumnInfo(name = "income_amt")
    //val incomeAmount: Double = 0.0,

    //@ColumnInfo(name = "expense_amt")
    //val expenseAmount: Double = 0.0,
)

fun Merchant.toMerchantNameAndDetails() = MerchantNameAndDetails(id, name, details)