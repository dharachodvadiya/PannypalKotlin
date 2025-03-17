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
import com.indie.apps.pennypal.data.module.user.UserInfoResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.io.IOException
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

    override suspend fun signOut(): Boolean {
        // oneTap.signOut().await()
        //firebaseAuth.signOut()
        return try {
            firebaseAuth.signOut()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun isSignedIn(): Boolean {
        return try {
            return firebaseAuth.currentUser != null
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getUserInfo(): UserInfoResult? {
        val currentUser = firebaseAuth.currentUser
        return if (currentUser != null) {
            UserInfoResult(email = currentUser.email!!)
        } else {
            null
        }
    }

    override suspend fun getSignInResult(credential: Credential): UserInfoResult? {
        return try {
            when (credential) {
                is CustomCredential -> {
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        val googleIdToken = googleIdTokenCredential.idToken
                        val googleCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                        val authResult = firebaseAuth.signInWithCredential(googleCredential).await()
                        //return UserInfoResult(authResult.user!!.email!!)

                        val user =
                            authResult.user ?: return null
                        UserInfoResult(user.email!!)
                    } else {
                        null
                    }
                }

                else -> null
            }
        } catch (e: GoogleIdTokenParsingException) {
            null
        } catch (e: IOException) {
            null
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getGoogleDrive(): Drive? {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                credential.selectedAccount = currentUser.email?.let { Account(it, "google.com") }
                Drive.Builder(
                    NetHttpTransport(), GsonFactory.getDefaultInstance(),
                    credential
                ).build()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun authorizeGoogleDrive(): AuthorizationResult? {
        return try {
            authorize.authorize(authorizationRequest).await()
        } catch (e: IOException) {
            null
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun authorizeGoogleDriveResult(intent: Intent): AuthorizationResult? {
        return try {
            authorize.getAuthorizationResultFromIntent(intent)
        } catch (e: Exception) {
            null
        }

    }
}