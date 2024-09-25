package com.indie.apps.pennypal.repository

import com.indie.apps.pennypal.util.Util
import javax.inject.Inject

class BillingRepositoryImpl @Inject constructor() : BillingRepository {
    override fun getSubscription() = Util.isSubscribed
    override fun setSubscription(isSubscribed: Boolean) {
        Util.isSubscribed = isSubscribed
    }

}