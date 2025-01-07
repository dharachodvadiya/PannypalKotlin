package com.indie.apps.pennypal.data.database.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration3to4 : Migration(3,4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        updateMerchantDataTable(db)
    }

    private fun updateMerchantDataTable(database: SupportSQLiteDatabase) {

        //make merchantId nullable
        database.execSQL("CREATE TABLE IF NOT EXISTS `merchant_data_new` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `merchant_id` INTEGER, `category_id` INTEGER NOT NULL, `payment_id` INTEGER NOT NULL, `date_milli` INTEGER NOT NULL, `details` TEXT, `amount` REAL NOT NULL, `type` INTEGER NOT NULL, FOREIGN KEY(`merchant_id`) REFERENCES `merchant`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`payment_id`) REFERENCES `payment_type`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`category_id`) REFERENCES `category`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_merchant_data_merchant_id` ON `merchant_data_new` (`merchant_id`)")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_merchant_data_payment_id` ON `merchant_data_new` (`payment_id`)")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_merchant_data_category_id` ON `merchant_data_new` (`category_id`)")

        // Copy data from old table to new table
        database.execSQL("INSERT INTO merchant_data_new (id, merchant_id, category_id, payment_id, date_milli, details, amount, type) SELECT id, merchant_id, category_id, payment_id, date_milli, details, amount, type FROM merchant_data")
        // Drop the old table
        database.execSQL("DROP TABLE merchant_data")
        // Rename the new table to the old table name
        database.execSQL("ALTER TABLE merchant_data_new RENAME TO merchant_data")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_merchant_data_merchant_id` ON `merchant_data` (`merchant_id`)")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_merchant_data_payment_id` ON `merchant_data` (`payment_id`)")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_merchant_data_category_id` ON `merchant_data` (`category_id`)")

    }

}
