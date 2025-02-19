package com.indie.apps.pennypal.data.database.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration4to5 : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        updateCategoryTable(db)
    }

    private fun updateCategoryTable(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE category ADD COLUMN icon_id INTEGER NOT NULL DEFAULT 1")
        database.execSQL("ALTER TABLE category ADD COLUMN icon_color_id INTEGER NOT NULL DEFAULT 1")

        updatePopulatedCategory(database)

    }

    private fun updatePopulatedCategory(database: SupportSQLiteDatabase) {
        for (id in 1..16) {
            database.execSQL("UPDATE category SET icon_id = $id, icon_color_id = $id WHERE id = $id;")
        }
    }

}
