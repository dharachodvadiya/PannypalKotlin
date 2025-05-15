package com.indie.apps.pennypal.repository

import android.os.Bundle

interface AnalyticRepository {
    fun logEvent(name: String, params: Bundle? = null)
}

