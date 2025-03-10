package com.indie.apps.pennypal.data.database.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration5to6 : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        updateUserTable(db)
    }

    private fun updateUserTable(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `user_new` (
            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
            `name` TEXT NOT NULL, 
            `email` TEXT, 
            `last_sync_date_milli` INTEGER NOT NULL, 
            `country_code` TEXT NOT NULL DEFAULT 'US', 
            `payment_id` INTEGER NOT NULL, 
            FOREIGN KEY(`payment_id`) REFERENCES `payment_type`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )
        """
        )

        database.execSQL(
            """
            INSERT INTO `user_new` (`id`, `name`, `email`, `last_sync_date_milli`, `payment_id`)
            SELECT `id`, `name`, `email`, `last_sync_date_milli`, `payment_id` FROM `user`
            """
        )
        // Drop the old table
        database.execSQL("DROP TABLE user")

        // Rename the new table to the old table's name
        database.execSQL("ALTER TABLE user_new RENAME TO user")
    }

}
