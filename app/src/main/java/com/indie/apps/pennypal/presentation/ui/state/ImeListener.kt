package com.indie.apps.pennypal.presentation.ui.state

import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.indie.apps.pennypal.util.Util.pxToDp

@Composable
fun rememberImeState(onImeSizeChange: (Float) -> Unit): State<Boolean> {
    val imeState = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    var initialViewHeight by remember { mutableIntStateOf(0) }

    val view = LocalView.current
    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val isKeyboardOpen = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true

            val imeBottomInsetPx = ViewCompat.getRootWindowInsets(view)
                ?.getInsets(WindowInsetsCompat.Type.ime())?.bottom ?: 0


            if (initialViewHeight == 0) {
                initialViewHeight = view.height
            }

            //val keyboardHeight = initialViewHeight - view.height
            imeState.value = isKeyboardOpen
            onImeSizeChange(imeBottomInsetPx.toFloat().pxToDp(context))

        }

        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {

            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }
    return imeState
}