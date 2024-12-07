package com.indie.apps.pennypal.repository

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.indie.apps.pennypal.data.module.UserInfoResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val googleSignInClient: GoogleSignInClient,
    @ApplicationContext private val context: Context,
) : AuthRepository {

    private val credential =
        GoogleAccountCredential.usingOAuth2(context, listOf(DriveScopes.DRIVE_FILE))
    private var googleSignInAccount: GoogleSignInAccount? =
        GoogleSignIn.getLastSignedInAccount(context)

    override suspend fun signInGoogle(): Intent {
        println("aaaaa start signin")
        return googleSignInClient.signInIntent
    }

    override suspend fun signOut() {
        googleSignInClient.signOut().await()
    }

    override suspend fun isSignedIn(): Boolean {
        println("aaaaa issignin ${googleSignInAccount != null}")
        return googleSignInAccount != null
    }

    override suspend fun getUserInfo(): UserInfoResult? {
        return googleSignInAccount?.let {
            UserInfoResult(
                email = googleSignInAccount!!.email,
                displayName = googleSignInAccount!!.displayName,
                idToken = googleSignInAccount!!.idToken
            )
        }
    }

    override suspend fun getSignInResult(intent: Intent): UserInfoResult {

        try {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)
            val account = task.getResult(ApiException::class.java)

            return UserInfoResult(
                email = account.email,
                displayName = account.displayName,
                idToken = account.idToken
            )
        } catch (e: ApiException) {
            throw SignInException("aaaa Google sign-in failed with error code: ${e.statusCode}", e)
        }
    }

    override suspend fun getGoogleDrive(): Drive? {
        return if (googleSignInAccount != null) {
            credential.setSelectedAccount(googleSignInAccount!!.account);
            Drive.Builder(
                NetHttpTransport(), GsonFactory.getDefaultInstance(),
                credential
            ).build()
        } else {
            null
        }
    }

}

class SignInException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)

