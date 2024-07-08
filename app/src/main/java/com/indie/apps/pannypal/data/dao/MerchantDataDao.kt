package com.indie.apps.pannypal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.data.entity.MerchantData
import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.data.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface MerchantDataDao : BaseDao<MerchantData> {

    //insert data -> update merchant income-expense
    //update data -> update merchant income-expense
    //delete data -> update merchant income-expense
    //get data

    @Delete
    suspend fun deleteMerchantData(merchantData: MerchantData) : Int

    @Delete
    suspend fun deleteMerchantsData(merchantsData: List<MerchantData>) : Int

    /*@Transaction
    @Query("SELECT * FROM merchant_data")
    suspend fun getMerchantsData(): List<MerchantData>*/

    @Transaction
    @Query("SELECT * FROM merchant_data ORDER BY id DESC LIMIT :limit OFFSET :offset")
    suspend fun getMerchantsData(limit: Int, offset: Int): List<MerchantData>
}