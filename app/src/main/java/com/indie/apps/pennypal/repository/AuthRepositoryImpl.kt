package com.indie.apps.pennypal.repository

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.indie.apps.pennypal.data.module.UserInfoResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val googleSignInClient: GoogleSignInClient,
) : AuthRepository {

    /*  private val oneTap = Identity.getSignInClient(context)
      private val signInRequest = BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(
          BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
              .setServerClientId(
                  "521870746383-i9k9ke25vpa3co2tqbmkslm4i4lo2nla.apps.googleusercontent.com"
              ).setFilterByAuthorizedAccounts(false)
              .build()
      ).setAutoSelectEnabled(true).build()*/


    override suspend fun signInGoogle(): Intent {
        return googleSignInClient.signInIntent
        //return oneTap.beginSignIn(signInRequest).await().pendingIntent.intentSender
    }

    override suspend fun signOut() {

        //val googleSignInClient = GoogleSignIn.getClient(context, gso)
        googleSignInClient.signOut().await()
        //oneTap.signOut().await()
    }

    override suspend fun isSignedIn(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getUserInfo(): UserInfoResult? {
        TODO("Not yet implemented")
    }

    override suspend fun getSignInResult(intent: Intent): UserInfoResult {
        val task: Task<GoogleSignInAccount> =
            GoogleSignIn.getSignedInAccountFromIntent(intent)
        val account = task.getResult(ApiException::class.java)
        return UserInfoResult(
            email = account.email,
            displayName = account.displayName,
            idToken = account.idToken
        )
    }

}

