package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.data.database.entity.User
import com.indie.apps.pennypal.data.module.UserWithPaymentName
import kotlinx.coroutines.flow.Flow

interface BillingRepository{

    fun getSubscription(): Boolean

    fun setSubscription(isSubscribed : Boolean): Unit
}