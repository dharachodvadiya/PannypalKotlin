package com.indie.apps.pennypal.repository

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class AnalyticRepositoryImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticRepository {
    override fun logEvent(name: String, params: Bundle?) {
        firebaseAnalytics.logEvent(name, params)
    }


}

