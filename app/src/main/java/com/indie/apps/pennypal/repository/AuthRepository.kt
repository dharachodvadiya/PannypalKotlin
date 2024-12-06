package com.indie.apps.pennypal.repository

import android.content.Intent
import com.indie.apps.pennypal.data.module.UserInfoResult

interface AuthRepository {
    suspend fun signInGoogle(): Intent
    suspend fun signOut()
    suspend fun isSignedIn(): Boolean
    suspend fun getUserInfo(): UserInfoResult?

    suspend fun getSignInResult(intent: Intent): UserInfoResult
}

