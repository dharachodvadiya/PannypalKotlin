package com.indie.apps.pennypal.repository

import android.accounts.Account
import android.content.Context
import android.content.Intent
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.AuthorizationResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
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

    private val authorize = Identity.getAuthorizationClient(context)

    private val firebaseAuth = Firebase.auth

    private val credential =
        GoogleAccountCredential.usingOAuth2(context, listOf(DriveScopes.DRIVE_FILE))

    private val googleIdOption: GetGoogleIdOption = GetGoogleIdOption
        .Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId("543082646910-is4lcvu1m56v6u2pjaeq1mqf2ndutved.apps.googleusercontent.com")
        .setAutoSelectEnabled(false)
        .build()

    override suspend fun signInGoogle(): GetCredentialRequest? {
        return try {
            GetCredentialRequest
                .Builder()
                .addCredentialOption(googleIdOption)
                .build()
        } catch (e: Exception) {
            null
        }

    }

    override suspend fun signOut() {
        // oneTap.signOut().await()
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

    override suspend fun getSignInResult(credential: Credential): UserInfoResult? {
        when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        val googleIdToken = googleIdTokenCredential.idToken
                        val googleCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                        val authResult = firebaseAuth.signInWithCredential(googleCredential).await()
                        return UserInfoResult(authResult.user!!.email!!)

                    } catch (e: GoogleIdTokenParsingException) {
                        println("aaa Received an invalid google id token response $e")
                        return null
                    }
                } else {
                    println("aaa Unexpected type of credential ... ${credential.type}")
                }
            }

            else -> {
                println("aaa Unexpected type of credential... ${credential.type}")
            }
        }
        return null
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