package com.indie.apps.pennypal.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration2to3 : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        //create category table
        db.execSQL("CREATE TABLE IF NOT EXISTS `category` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL COLLATE NOCASE, `pre_added` INTEGER NOT NULL, `soft_delete` INTEGER NOT NULL, `type` INTEGER NOT NULL)")
        populateCategoryDb(db)
        updateMerchantDataTable(db)
        updateUserTable(db)
        updateMerchantTable(db)
    }

    private fun updateMerchantDataTable(database: SupportSQLiteDatabase) {
        // Add the foreign key column to payment_type table
        database.execSQL("ALTER TABLE merchant_data ADD COLUMN category_id INTEGER NOT NULL DEFAULT 1")
        // Add foreign key constraint
        database.execSQL("CREATE TABLE IF NOT EXISTS `merchant_data_new` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `merchant_id` INTEGER NOT NULL, `category_id` INTEGER NOT NULL, `payment_id` INTEGER NOT NULL, `date_milli` INTEGER NOT NULL, `details` TEXT, `amount` REAL NOT NULL, `type` INTEGER NOT NULL, FOREIGN KEY(`merchant_id`) REFERENCES `merchant`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`payment_id`) REFERENCES `payment_type`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`category_id`) REFERENCES `category`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )")
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

    private fun populateCategoryDb(database: SupportSQLiteDatabase) {
        // Insert default values into payment_mode table
        database.execSQL("INSERT INTO category (name, pre_added, soft_delete, type) VALUES ('Other',1,0,0)")
        database.execSQL("INSERT INTO category (name, pre_added, soft_delete, type) VALUES ('Bills and Utilities',1,0,-1)")
        database.execSQL("INSERT INTO category (name, pre_added, soft_delete, type) VALUES ('Education',1,0,-1)")
        database.execSQL("INSERT INTO category (name, pre_added, soft_delete, type) VALUES ('Entertainment',1,0,-1)")
        database.execSQL("INSERT INTO category (name, pre_added, soft_delete, type) VALUES ('Food and Dining',1,0,-1)")
        database.execSQL("INSERT INTO category (name, pre_added, soft_delete, type) VALUES ('Gift and Donation',1,0,-1)")
        database.execSQL("INSERT INTO category (name, pre_added, soft_delete, type) VALUES ('Insurance',1,0,-1)")
        database.execSQL("INSERT INTO category (name, pre_added, soft_delete, type) VALUES ('Investments',1,0,-1)")
        database.execSQL("INSERT INTO category (name, pre_added, soft_delete, type) VALUES ('Medical',1,0,-1)")
        database.execSQL("INSERT INTO category (name, pre_added, soft_delete, type) VALUES ('Personal Care',1,0,-1)")
        database.execSQL("INSERT INTO category (name, pre_added, soft_delete, type) VALUES ('Rent',1,0,0)")
        database.execSQL("INSERT INTO category (name, pre_added, soft_delete, type) VALUES ('Shopping',1,0,-1)")
        database.execSQL("INSERT INTO category (name, pre_added, soft_delete, type) VALUES ('Taxes',1,0,-1)")
        database.execSQL("INSERT INTO category (name, pre_added, soft_delete, type) VALUES ('Travelling',1,0,-1)")
        database.execSQL("INSERT INTO category (name, pre_added, soft_delete, type) VALUES ('Salary',1,0,1)")
        database.execSQL("INSERT INTO category (name, pre_added, soft_delete, type) VALUES ('Rewards',1,0,1)")
    }

    private fun updateMerchantTable(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `merchant_new` (
            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
            `name` TEXT NOT NULL COLLATE NOCASE, 
            `phone_num` TEXT, 
            `country_code` TEXT, 
            `details` TEXT)
        """)

        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_merchant_name` ON `merchant_new` (`name`)")

        database.execSQL(
            """
            INSERT INTO `merchant_new` (`id`, `name`, `phone_num`, `country_code`, `details`)
            SELECT `id`, `name`, `phone_num`, `country_code`, `details` FROM `merchant`
            """
        )
        // Drop the old table
        database.execSQL("DROP TABLE merchant")

        // Rename the new table to the old table's name
        database.execSQL("ALTER TABLE merchant_new RENAME TO merchant")
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_merchant_name` ON `merchant` (`name`)")

    }

    private fun updateUserTable(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `user_new` (
            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
            `name` TEXT NOT NULL, 
            `email` TEXT, 
            `last_sync_date_milli` INTEGER NOT NULL, 
            `currency` TEXT NOT NULL, 
            `country_code` TEXT NOT NULL DEFAULT 'USD', 
            `payment_id` INTEGER NOT NULL, 
            FOREIGN KEY(`payment_id`) REFERENCES `payment_type`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )
        """)

        database.execSQL(
            """
            INSERT INTO `user_new` (`id`, `name`, `email`, `last_sync_date_milli`, `currency`, `payment_id`)
            SELECT `id`, `name`, `email`, `last_sync_date_milli`, `currency`, `payment_id` FROM `user`
            """
        )
        // Drop the old table
        database.execSQL("DROP TABLE user")

        // Rename the new table to the old table's name
        database.execSQL("ALTER TABLE user_new RENAME TO user")

    }
}
