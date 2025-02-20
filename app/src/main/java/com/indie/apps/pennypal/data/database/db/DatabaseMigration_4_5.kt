package com.indie.apps.pennypal.data.database.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration4to5 : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        updateCategoryTable(db)
        updateMerchantTable(db)
    }

    private fun updateCategoryTable(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE category ADD COLUMN icon_id INTEGER NOT NULL DEFAULT 1")
        database.execSQL("ALTER TABLE category ADD COLUMN icon_color_id INTEGER NOT NULL DEFAULT 1")

        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS category_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL COLLATE NOCASE UNIQUE,
                pre_added INTEGER NOT NULL DEFAULT 0,
                soft_delete INTEGER NOT NULL DEFAULT 0,
                type INTEGER NOT NULL DEFAULT 0,
                icon_id INTEGER NOT NULL DEFAULT 1,
                icon_color_id INTEGER NOT NULL DEFAULT 1
            )
        """.trimIndent()
        )
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_category_name` ON `category_new` (`name`)")

        // Step 2: Copy existing data into new table
        database.execSQL(
            """
            INSERT INTO category_new (id, name, pre_added, soft_delete, type, icon_id, icon_color_id)
            SELECT id, name, pre_added, soft_delete, type, icon_id, icon_color_id FROM category
        """.trimIndent()
        )

        // Step 3: Drop old table
        database.execSQL("DROP TABLE category")

        // Step 4: Rename new table to old table name
        database.execSQL("ALTER TABLE category_new RENAME TO category")
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_category_name` ON `category` (`name`)")


        updatePopulatedCategory(database)

    }

    private fun updatePopulatedCategory(database: SupportSQLiteDatabase) {
        for (id in 1..16) {
            database.execSQL("UPDATE category SET icon_id = $id, icon_color_id = $id WHERE id = $id;")
        }
    }

    private fun updateMerchantTable(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE merchant ADD COLUMN soft_delete INTEGER NOT NULL DEFAULT 0")
    }


}
