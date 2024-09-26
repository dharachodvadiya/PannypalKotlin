package com.indie.apps.pennypal.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var appUpdateInfoGlobal: AppUpdateInfo? = null
    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var listener: InstallStateUpdatedListener
    private lateinit var updateResultLauncher: ActivityResultLauncher<Intent>

    // State for managing update progress
    private var isUpdating by mutableStateOf(false)
    private var updateProgress by mutableIntStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        appUpdateManager = AppUpdateManagerFactory.create(this)
        setupUpdateResultLauncher()
        setupUpdateListener()

        checkForUpdate()

        setContent {
            PennyPalApp() // Pass the state to the app
        }
    }

    private fun setupUpdateResultLauncher() {
        updateResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    RESULT_OK -> Log.d("InApp-update", "Update Result: OK")
                    RESULT_CANCELED -> Log.d("InApp-update", "Update Result: Canceled")
                    else -> Log.d("InApp-update", "Update Result: Failed")
                }
            }
    }

    private fun setupUpdateListener() {
        listener = InstallStateUpdatedListener { installState ->
            when (installState.installStatus()) {
                InstallStatus.DOWNLOADED -> {
                    removeUpdateListener()
                    isUpdating = false
                    appUpdateManager.completeUpdate()
                }

                InstallStatus.INSTALLED -> {
                    // Handle any post-installation tasks if needed
                }

                InstallStatus.DOWNLOADING -> {
                    isUpdating = true
                    // Calculate download progress
                    val totalBytesToDownload = installState.totalBytesToDownload()
                    val bytesDownloaded = installState.bytesDownloaded()
                    if (totalBytesToDownload > 0) {
                        updateProgress = (bytesDownloaded * 100 / totalBytesToDownload).toInt()
                    }
                }

                InstallStatus.FAILED -> {
                    isUpdating = false
                    Log.e("InApp-update", "Update Failed")
                }

                else -> {
                    isUpdating = false
                }
            }
        }
        appUpdateManager.registerListener(listener)
    }

    private fun checkForUpdate() {
        Log.d("InApp-update", "Checking for updates")
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            Log.d("InApp-update", "Update Availability: ${appUpdateInfo.updateAvailability()}")
            Log.d(
                "InApp-update",
                "Update Type Allowed: ${appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)}"
            )

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                appUpdateInfoGlobal = appUpdateInfo
                Log.d("InApp-update", "Update available")
                initiateUpdate()
            } else {
                Log.d("InApp-update", "No update available")
            }
        }.addOnFailureListener { exception ->
            Log.e("InApp-update", "Error checking for updates: ${exception.message}")
        }
    }

    private fun initiateUpdate() {
        appUpdateInfoGlobal?.let { appUpdateInfo ->
            Log.d("InApp-update", "Initiating update...")
            isUpdating = true // Set updating state
            appUpdateManager.startUpdateFlow(
                appUpdateInfo,
                this,
                AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
            )
        }
    }

    private fun removeUpdateListener() {
        appUpdateManager.unregisterListener(listener)
    }
}



