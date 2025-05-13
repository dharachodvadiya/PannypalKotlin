package com.indie.apps.pennypal.data.database.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration7to8() : Migration(7, 8) {
    override fun migrate(db: SupportSQLiteDatabase) {

        updateMerchantDataTable(db)
    }

    private fun updateMerchantDataTable(
        database: SupportSQLiteDatabase
    ) {
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_merchant_data_date_milli` ON `merchant_data` (`date_milli`)")

    }
}
