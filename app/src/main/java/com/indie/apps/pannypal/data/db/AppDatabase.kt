package com.indie.apps.pannypal.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.indie.apps.pannypal.data.dao.MerchantDao
import com.indie.apps.pannypal.data.dao.MerchantDataDao
import com.indie.apps.pannypal.data.dao.PaymentDao
import com.indie.apps.pannypal.data.dao.UserDao
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.data.entity.User

@Database(
    entities = [
        User::class,
        Payment::class,
        Merchant::class,
        MerchantData::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract val userDao : UserDao
    abstract val paymentDao : PaymentDao
    abstract val merchantDao : MerchantDao
    abstract val merchantDataDao : MerchantDataDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pannypal_money_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}