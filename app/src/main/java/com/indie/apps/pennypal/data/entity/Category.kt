package com.indie.apps.pennypal.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "category"
)
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val name: String,

    @ColumnInfo(name = "pre_added")
    val preAdded: Int = 0,

    @ColumnInfo(name = "soft_delete")
    val softDeleted: Int = 0,

    val type: Int = 0 //-1 = expense, 1 = income , 0 = both
)
