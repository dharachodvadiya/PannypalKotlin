package com.indie.apps.pennypal.repository

import android.accounts.Account
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.AuthorizationResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.indie.apps.pennypal.data.module.UserInfoResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : AuthRepository {

    private val scopes = listOf(Scope(DriveScopes.DRIVE_FILE))
    private val authorizationRequest =
        AuthorizationRequest.builder().setRequestedScopes(scopes).build()

    private val oneTap = Identity.getSignInClient(context)
    private val signInRequest = BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(
        BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
            .setServerClientId(
                "543082646910-is4lcvu1m56v6u2pjaeq1mqf2ndutved.apps.googleusercontent.com"
            ).setFilterByAuthorizedAccounts(false).build()
    ).setAutoSelectEnabled(true).build()

    private val authorize = Identity.getAuthorizationClient(context)

    private val firebaseAuth = Firebase.auth

    private val credential =
        GoogleAccountCredential.usingOAuth2(context, listOf(DriveScopes.DRIVE_FILE))


    override suspend fun signInGoogle(): IntentSender? {
        return try {
            oneTap.beginSignIn(signInRequest).await().pendingIntent.intentSender
        } catch (e: Exception) {
            null
        }

    }

    override suspend fun signOut() {
        oneTap.signOut().await()
        firebaseAuth.signOut()
    }

    override suspend fun isSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun getUserInfo(): UserInfoResult? {
        val currentUser = firebaseAuth.currentUser
        return if (currentUser != null) {
            UserInfoResult(
                email = currentUser.email!!
            )
        } else {
            null
        }
    }

    override suspend fun getSignInResult(intent: Intent): UserInfoResult? {
        try {
            val credential = oneTap.getSignInCredentialFromIntent(intent)
            val googleIdToken = credential.googleIdToken
            val googleCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            val authResult = firebaseAuth.signInWithCredential(googleCredential).await()
            return UserInfoResult(authResult.user!!.email!!)
        } catch (e: Exception) {
            return null
        }

    }

    override suspend fun getGoogleDrive(): Drive? {
        val currentUser = firebaseAuth.currentUser
        return if (currentUser != null) {
            credential.selectedAccount = currentUser.email?.let { Account(it, "google.com") }
            Drive.Builder(
                NetHttpTransport(), GsonFactory.getDefaultInstance(),
                credential
            ).build()
        } else {
            null
        }
    }

    override suspend fun authorizeGoogleDrive(): AuthorizationResult {
        return authorize.authorize(authorizationRequest).await()
    }

    override suspend fun authorizeGoogleDriveResult(intent: Intent): AuthorizationResult? {
        return try {
            authorize.getAuthorizationResultFromIntent(intent)
        } catch (e: Exception) {
            null
        }

    }
}