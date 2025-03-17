package com.indie.apps.pennypal.data.database.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.indie.apps.cpp.data.repository.CountryRepository

class Migration6to7(private val countryRepository: CountryRepository) : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        createBaseCurrencyTable(db)
        updateMerchantDataTable(db, countryRepository)
        createExchangeRateTable(db)
    }

    private fun createBaseCurrencyTable(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `base_currency` (
            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
            `currency_country_code` TEXT NOT NULL,
            `currency_symbol` TEXT NOT NULL
            )
            """.trimIndent()
        )
        database.execSQL(
            """
            CREATE UNIQUE INDEX IF NOT EXISTS 
            `index_base_currency_currency_country_code` ON `base_currency` (`currency_country_code`)
            """.trimIndent()
        )

        val countryCode =
            database.query("SELECT country_code FROM user LIMIT 1")
                .use { cursor ->
                    if (cursor.moveToFirst()) cursor.getString(0) else "US" // Default to "US"
                }
        val symbol = countryRepository.getCurrencySymbolFromCountryCode(countryCode)


        database.execSQL(
            """
    INSERT INTO base_currency (currency_country_code, currency_symbol) 
    VALUES (?, ?)
    """.trimIndent(),
            arrayOf(countryCode, symbol) // Pass parameters safely
        )
    }

    private fun updateMerchantDataTable(
        database: SupportSQLiteDatabase,
        countryRepository: CountryRepository
    ) {

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
            `original_currency_id` INTEGER NOT NULL, 
            `original_amount` REAL NOT NULL,
             `type` INTEGER NOT NULL, 
             FOREIGN KEY(`merchant_id`) REFERENCES `merchant`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , 
             FOREIGN KEY(`payment_id`) REFERENCES `payment_type`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION , 
             FOREIGN KEY(`category_id`) REFERENCES `category`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION , 
             FOREIGN KEY(`base_currency_id`) REFERENCES `base_currency`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION ,
             FOREIGN KEY(`original_currency_id`) REFERENCES `base_currency`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION 
             )
             """.trimIndent()
        )
        val baseCurrencyId = database.query("SELECT id FROM base_currency LIMIT 1").use { cursor ->
            if (cursor.moveToFirst()) cursor.getLong(0) else 1L // Default to 1 if no data
        }
        val countryCode =
            database.query("SELECT currency_country_code FROM base_currency WHERE id = $baseCurrencyId LIMIT 1")
                .use { cursor ->
                    if (cursor.moveToFirst()) cursor.getString(0) else "US" // Default to "US"
                }
        val symbol = countryRepository.getCurrencySymbolFromCountryCode(countryCode)

        database.execSQL(
            """
            INSERT INTO merchant_data_new (
                id, merchant_id, category_id, payment_id, date_milli, details, amount, 
                base_currency_id, original_amount, original_currency_id, type
            )
            SELECT 
                id, merchant_id, category_id, payment_id, date_milli, details, amount, 
                $baseCurrencyId, amount, $baseCurrencyId, type 
            FROM merchant_data
        """.trimIndent()
        )

        database.execSQL("DROP TABLE merchant_data;")

        database.execSQL("ALTER TABLE merchant_data_new RENAME TO merchant_data;")

        database.execSQL("CREATE INDEX IF NOT EXISTS `index_merchant_data_merchant_id` ON `merchant_data` (`merchant_id`)")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_merchant_data_payment_id` ON `merchant_data` (`payment_id`)")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_merchant_data_category_id` ON `merchant_data` (`category_id`)")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_merchant_data_base_currency_id` ON `merchant_data` (`base_currency_id`)")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_merchant_data_original_currency_id` ON `merchant_data` (`original_currency_id`)")


    }

    private fun createExchangeRateTable(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `exchange_rates` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `from_currency` TEXT NOT NULL, `to_currency` TEXT NOT NULL, `rate` REAL NOT NULL, `last_updated` INTEGER NOT NULL)")

    }


}
