package com.indie.apps.pennypal.data.db

import android.annotation.SuppressLint
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.indie.apps.cpp.data.repository.CountryRepository

class Migration1to2(private val countryRepository: CountryRepository) : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {

        // Create the payment_mode table
        db.execSQL("CREATE TABLE IF NOT EXISTS `payment_mode` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL COLLATE NOCASE)")
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_payment_mode_name` ON `payment_mode` (`name`)")

        populatePaymentMode(db)

        updateUserTable(db, countryRepository)

        updatePaymentTypeTable(db)

        updatePopulatedPayment(db)
    }
}

@SuppressLint("Range")
fun updateUserTable(database: SupportSQLiteDatabase, countryRepository: CountryRepository) {
    // Add the default payment_id column to user table
    database.execSQL("ALTER TABLE user ADD COLUMN payment_id INTEGER NOT NULL DEFAULT 1")
    database.execSQL("ALTER TABLE user ADD COLUMN country_code TEXT")


    val cursor = database.query("SELECT currency FROM user WHERE id = 1")

    try {
        var currency = ""
        while (cursor.moveToNext()) {
            currency = cursor.getString(cursor.getColumnIndex("currency"))
        }

        val countryCode = countryRepository.getCountryCodeFromCurrencyCode(currency)

        database.execSQL("UPDATE user SET country_code = '$countryCode' WHERE ID = 1")
        // Add foreign key constraint
        database.execSQL("CREATE TABLE IF NOT EXISTS `user_new` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `email` TEXT, `last_sync_date_milli` INTEGER NOT NULL, `income_amt` REAL NOT NULL, `expense_amt` REAL NOT NULL, `currency` TEXT NOT NULL, `country_code` TEXT NOT NULL, `payment_id` INTEGER NOT NULL, FOREIGN KEY(`payment_id`) REFERENCES `payment_type`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )")
        // Copy data from old table to new table
        database.execSQL("INSERT INTO user_new (id, name, email, last_sync_date_milli, income_amt, expense_amt, currency, country_code, payment_id) SELECT id, name, email, last_sync_date_milli, income_amt, expense_amt, currency, country_code, payment_id FROM user")
        // Drop the old table
        database.execSQL("DROP TABLE user")
        // Rename the new table to the old table name
        database.execSQL("ALTER TABLE user_new RENAME TO user")
    } finally {
        cursor.close()
    }


}

fun updatePaymentTypeTable(database: SupportSQLiteDatabase) {
    // Add the foreign key column to payment_type table
    database.execSQL("ALTER TABLE payment_type ADD COLUMN mode_id INTEGER NOT NULL DEFAULT 1")
    database.execSQL("ALTER TABLE payment_type ADD COLUMN soft_delete INTEGER NOT NULL DEFAULT 0")
    // Add foreign key constraint
    database.execSQL("CREATE TABLE IF NOT EXISTS `payment_type_new` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL COLLATE NOCASE, `pre_added` INTEGER NOT NULL, `soft_delete` INTEGER NOT NULL,  `mode_id` INTEGER NOT NULL, FOREIGN KEY(`mode_id`) REFERENCES `payment_mode`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )")
    database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_payment_type_name` ON `payment_type_new` (`name`)")
    // Copy data from old table to new table
    database.execSQL("INSERT INTO payment_type_new (id, name, pre_added, mode_id, soft_delete) SELECT id, name, pre_added, mode_id, soft_delete FROM payment_type")
    // Drop the old table
    database.execSQL("DROP TABLE payment_type")
    // Rename the new table to the old table name
    database.execSQL("ALTER TABLE payment_type_new RENAME TO payment_type")
    database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_payment_type_name` ON `payment_type` (`name`)")
}

fun updatePopulatedPayment(database: SupportSQLiteDatabase) {
    database.execSQL("UPDATE payment_type SET mode_id = 2 WHERE id = '1';")
    database.execSQL("UPDATE payment_type SET mode_id = 3, name='Bank' WHERE id = '2';")
    database.execSQL("UPDATE payment_type SET mode_id = 4 WHERE id = '3';")
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