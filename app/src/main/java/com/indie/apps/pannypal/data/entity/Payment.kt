package com.indie.apps.pannypal.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "payment_type",
    indices = [Index(value = ["name"], unique = true)]
)
data class Payment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    @ColumnInfo(name = "pre_added")
    val preAdded : Int
)
