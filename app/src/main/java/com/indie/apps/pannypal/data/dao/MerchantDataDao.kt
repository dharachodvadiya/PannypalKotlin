package com.indie.apps.pannypal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
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
    fun deleteMerchantData(merchantData: MerchantData) : Int

    @Delete
    fun deleteMerchantsData( vararg merchantsData: List<MerchantData>) : Int

    @Query("SELECT * FROM merchant_data")
    fun getMerchantsData(): List<MerchantData>
}