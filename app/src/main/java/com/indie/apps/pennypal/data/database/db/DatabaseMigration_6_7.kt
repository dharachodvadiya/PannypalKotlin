package com.indie.apps.pennypal.data.database.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration6to7 : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        println("aaaa migration 6 to 7")
        createBaseCurrencyTable(db)
        updateMerchantDataTable(db)
    }

    private fun createBaseCurrencyTable(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `base_currency` (
            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
            `currency_country_code` TEXT NOT NULL
            )
            """.trimIndent()
        )
        database.execSQL(
            """
            CREATE UNIQUE INDEX IF NOT EXISTS 
            `index_base_currency_currency_country_code` ON `base_currency` (`currency_country_code`)
            """.trimIndent()
        )


        database.execSQL(
            """
            INSERT INTO base_currency (currency_country_code)
            SELECT COALESCE((SELECT country_code FROM user LIMIT 1), 'US')
        """.trimIndent()
        )
    }

    private fun updateMerchantDataTable(database: SupportSQLiteDatabase) {

        /*   database.execSQL("""
               ALTER TABLE merchant_data ADD COLUMN base_currency_id INTEGER NOT NULL DEFAULT 1
           """.trimIndent())
           database.execSQL("""
               UPDATE merchant_data
               SET base_currency_id = (SELECT id FROM base_currency LIMIT 1)
           """.trimIndent())

           // Add 'original_amount' column to merchant_data
           database.execSQL("ALTER TABLE merchant_data ADD COLUMN original_amount REAL NOT NULL DEFAULT 0.0")
           // Copy 'amount' to 'original_amount' for existing rows
           database.execSQL("UPDATE merchant_data SET original_amount = amount WHERE original_amount = 0.0")

           // Add 'currency_country_code' column to merchant_data
           database.execSQL("ALTER TABLE merchant_data ADD COLUMN currency_country_code TEXT")*/

        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `merchant_data_new` (
            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
            `merchant_id` INTEGER, 
            `category_id` INTEGER NOT NULL, 
            `payment_id` INTEGER NOT NULL, 
            `date_milli` INTEGER NOT NULL, 
            `details` TEXT, 
            `amount` REAL NOT NULL, 
            `base_currency_id` INTEGER NOT NULL, 
            `original_amount` REAL NOT NULL,
             `currency_country_code` TEXT, 
             `type` INTEGER NOT NULL, 
             FOREIGN KEY(`merchant_id`) REFERENCES `merchant`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , 
             FOREIGN KEY(`payment_id`) REFERENCES `payment_type`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION , 
             FOREIGN KEY(`category_id`) REFERENCES `category`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION , 
             FOREIGN KEY(`base_currency_id`) REFERENCES `base_currency`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION 
             )
             """.trimIndent()
        )

        // Copy old data
        database.execSQL(
            """
    INSERT INTO merchant_data_new 
    (id, merchant_id, category_id, payment_id, date_milli, details, amount, base_currency_id, original_amount, currency_country_code, type)
    SELECT 
    id, merchant_id, category_id, payment_id, date_milli, details, amount, (SELECT id FROM base_currency LIMIT 1), amount, NULL, type FROM merchant_data;
""".trimIndent()
        )

        database.execSQL("DROP TABLE merchant_data;")

        database.execSQL("ALTER TABLE merchant_data_new RENAME TO merchant_data;")

        database.execSQL("CREATE INDEX IF NOT EXISTS `index_merchant_data_merchant_id` ON `merchant_data` (`merchant_id`)")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_merchant_data_payment_id` ON `merchant_data` (`payment_id`)")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_merchant_data_category_id` ON `merchant_data` (`category_id`)")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_merchant_data_base_currency_id` ON `merchant_data` (`base_currency_id`)")


    }


}
