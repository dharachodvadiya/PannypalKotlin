package com.indie.apps.pennypal.repository

import android.content.Intent
import androidx.credentials.Credential
import androidx.credentials.GetCredentialRequest
import com.google.android.gms.auth.api.identity.AuthorizationResult
import com.google.api.services.drive.Drive
import com.indie.apps.pennypal.data.module.UserInfoResult

interface AuthRepository {
    suspend fun signInGoogle(): GetCredentialRequest?
    suspend fun signOut()
    suspend fun isSignedIn(): Boolean
    suspend fun getUserInfo(): UserInfoResult?

    suspend fun getSignInResult(credential: Credential): UserInfoResult?

    suspend fun getGoogleDrive(): Drive?

    suspend fun authorizeGoogleDrive(): AuthorizationResult
    suspend fun authorizeGoogleDriveResult(intent: Intent): AuthorizationResult?
}

