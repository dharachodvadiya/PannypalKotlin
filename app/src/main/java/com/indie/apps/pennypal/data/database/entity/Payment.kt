package com.indie.apps.pennypal.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "payment_type",
    foreignKeys = [
        ForeignKey(
            entity = PaymentMode::class,
            parentColumns = ["id"],
            childColumns = ["mode_id"],
            onDelete = ForeignKey.NO_ACTION
        )
    ],
    indices = [Index(value = ["name"], unique = true)]
)
data class Payment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val name: String,

    @ColumnInfo(name = "pre_added")
    val preAdded: Int = 0,

    @ColumnInfo(name = "mode_id")
    val modeId: Long = 1L, //0= other, 1 = cash, 2 = bank, 3 = card , 4 = cheque, 5 = net-banking, 6 = upi

    @ColumnInfo(name = "soft_delete")
    val softDeleted: Int = 0
)
