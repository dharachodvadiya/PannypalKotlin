package com.indie.apps.pannypal.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "payment_type"
)
data class Payment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,
)
