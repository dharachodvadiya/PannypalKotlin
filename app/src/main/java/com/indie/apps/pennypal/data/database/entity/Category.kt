package com.indie.apps.pennypal.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.indie.apps.pennypal.data.module.category.CategoryAmount

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

    val type: Int = 0, //-1 = expense, 1 = income , 0 = both

    @ColumnInfo(name = "icon_id")
    val iconId: Int = 1,

    @ColumnInfo(name = "icon_color_id")
    val iconColorId: Int = 1,
)

fun Category.toCategoryAmount() = CategoryAmount(id, name, 0.0, type, iconId, iconColorId)
