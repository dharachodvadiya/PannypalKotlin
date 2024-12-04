package com.indie.apps.pennypal.repository

interface BillingRepository {

    fun getSubscription(): Boolean

    fun setSubscription(isSubscribed: Boolean)
}