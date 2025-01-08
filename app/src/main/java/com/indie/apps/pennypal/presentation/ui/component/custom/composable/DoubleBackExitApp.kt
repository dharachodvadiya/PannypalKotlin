package com.indie.apps.pennypal.presentation.ui.component.custom.composable

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.showToast

@Composable
fun DoubleBackExitApp() {
    val backPressedTime = remember { mutableLongStateOf(0L) }

    val context = LocalContext.current
    val exitMessage = stringResource(R.string.press_back_again_to_exit_app)

    // Handle the back press
    BackHandler {
        val currentTime = System.currentTimeMillis()

        // Check if back button was pressed twice in a short time window (e.g., 2 seconds)
        if (currentTime - backPressedTime.longValue <= 2000) {
            // Exit the app
            (context as? Activity)?.finish()
        } else {
            // Show prompt to the user
            backPressedTime.longValue = currentTime
            context.showToast(exitMessage)
        }
    }

}