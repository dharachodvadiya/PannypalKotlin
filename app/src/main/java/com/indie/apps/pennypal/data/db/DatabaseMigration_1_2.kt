package com.indie.apps.pennypal.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {

        // Create the payment_mode table
        database.execSQL("CREATE TABLE IF NOT EXISTS `payment_mode` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL COLLATE NOCASE)");
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_payment_mode_name` ON `payment_mode` (`name`)");

        populatePaymentMode(database)

        // Add the foreign key column to payment_type table
        database.execSQL("ALTER TABLE payment_type ADD COLUMN mode_id INTEGER NOT NULL DEFAULT 0")

        // Add foreign key constraint
        database.execSQL("CREATE TABLE IF NOT EXISTS `payment_type_new` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL COLLATE NOCASE, `pre_added` INTEGER NOT NULL, `mode_id` INTEGER NOT NULL, FOREIGN KEY(`mode_id`) REFERENCES `payment_mode`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_payment_type_name` ON `payment_type_new` (`name`)");

        // Copy data from old table to new table
        database.execSQL("INSERT INTO payment_type_new (id, name, pre_added, mode_id) SELECT id, name, pre_added, mode_id FROM payment_type")

        // Drop the old table
        database.execSQL("DROP TABLE payment_type")

        // Rename the new table to the old table name
        database.execSQL("ALTER TABLE payment_type_new RENAME TO payment_type")
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_payment_type_name` ON `payment_type` (`name`)");

        updatePopulatedPayment(database)
    }
}

fun updatePopulatedPayment(database: SupportSQLiteDatabase) {
    database.execSQL("UPDATE payment_type SET mode_id = 1 WHERE id = '0';")
    database.execSQL("UPDATE payment_type SET mode_id = 2 WHERE id = '1';")
    database.execSQL("UPDATE payment_type SET mode_id = 3 WHERE id = '2';")
}

fun populatePaymentMode(database: SupportSQLiteDatabase) {
    // Insert default values into payment_mode table
    database.execSQL("INSERT INTO payment_mode (name) VALUES ('Other')")
    database.execSQL("INSERT INTO payment_mode (name) VALUES ('Cash')")
    database.execSQL("INSERT INTO payment_mode (name) VALUES ('Bank')")
    database.execSQL("INSERT INTO payment_mode (name) VALUES ('Card')")
    database.execSQL("INSERT INTO payment_mode (name) VALUES ('Cheque')")
    database.execSQL("INSERT INTO payment_mode (name) VALUES ('Net-banking')")
    database.execSQL("INSERT INTO payment_mode (name) VALUES ('Upi')")
}