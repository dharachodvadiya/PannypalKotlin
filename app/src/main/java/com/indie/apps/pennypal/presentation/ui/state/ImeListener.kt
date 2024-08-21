package com.indie.apps.pennypal.presentation.ui.state

import android.view.ViewTreeObserver
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.toRecIntSize
import com.indie.apps.pennypal.util.Util.pxToDp

@Composable
fun rememberImeState(onImeSizeChange : (Float) -> Unit) : State<Boolean> {
    val imeState = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    var initialViewHeight by remember { mutableStateOf(0) }

    val view = LocalView.current
    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val isKeyboardOpen = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true

            val imeBottomInsetPx = ViewCompat.getRootWindowInsets(view)?.getInsets(WindowInsetsCompat.Type.ime())?.bottom ?: 0


            if(initialViewHeight == 0)
            {
                initialViewHeight = view.height
            }

            //val keyboardHeight = initialViewHeight - view.height
            imeState.value = isKeyboardOpen
            onImeSizeChange(imeBottomInsetPx.toFloat().pxToDp(context))
            /*println("aaaaaa view  = ${view.height}")
            println("aaaaaa bottomInset  = $imeBottomInsetPx")
            println("aaaaaa current view  = ${view.height}")*/

        }

        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {

            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }
    return imeState
}