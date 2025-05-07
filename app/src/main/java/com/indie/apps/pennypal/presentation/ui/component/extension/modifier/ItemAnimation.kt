package com.indie.apps.pennypal.presentation.ui.component.extension.modifier

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("ModifierFactoryUnreferencedReceiver", "CoroutineCreationDuringComposition")
@Composable
fun Modifier.addAnim(
    scope: CoroutineScope,
    onComplete: () -> Unit = {}
): Modifier {

    val scale = remember {
        Animatable(0f)
    }

    scope.launch {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(Util.ADD_ITEM_ANIM_TIME)
        )
    }
    if (scale.value >= 0.99f) {
        onComplete()
    }
    return Modifier.scale(scale.value)
}

@SuppressLint("ModifierFactoryUnreferencedReceiver", "CoroutineCreationDuringComposition")
@Composable
fun Modifier.addAnimTopDown(
    scope: CoroutineScope,
    onComplete: () -> Unit = {}
): Modifier {

    val scale = remember {
        Animatable(0f)
    }

    scope.launch {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(Util.ADD_ITEM_ANIM_TIME)
        )
    }
    if (scale.value >= 0.99f) {
        onComplete()
    }
    return Modifier.graphicsLayer {
        scaleX = 1f
        scaleY = scale.value
        transformOrigin = TransformOrigin(0.5f, 0f) // Center horizontally, top vertically
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver", "CoroutineCreationDuringComposition")
@Composable
fun Modifier.addAnimTopDownToLeft(
    scope: CoroutineScope,
    onComplete: () -> Unit = {}
): Modifier {

    val scale = remember {
        Animatable(0f)
    }

    scope.launch {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(Util.ADD_ITEM_ANIM_TIME)
        )
    }
    if (scale.value >= 0.99f) {
        onComplete()
    }
    return Modifier.graphicsLayer {
        scaleX = scale.value
        scaleY = scale.value
        transformOrigin = TransformOrigin(1f, 0f) // Center horizontally, top vertically
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver", "CoroutineCreationDuringComposition")
@Composable
fun Modifier.addAnimTopDownToRight(
    scope: CoroutineScope,
    onComplete: () -> Unit = {}
): Modifier {

    val scale = remember {
        Animatable(0f)
    }

    scope.launch {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(Util.ADD_ITEM_ANIM_TIME)
        )
    }
    if (scale.value >= 0.99f) {
        onComplete()
    }
    return Modifier.graphicsLayer {
        scaleX = scale.value
        scaleY = scale.value
        transformOrigin = TransformOrigin(0f, 0f) // Center horizontally, top vertically
    }
}

@SuppressLint("CoroutineCreationDuringComposition", "ModifierFactoryUnreferencedReceiver")
@Composable
fun Modifier.editAnim(
    scope: CoroutineScope,
    itemAnimateColor: Animatable<Color, AnimationVector4D>,
    onComplete: () -> Unit = {}
): Modifier {
    val baseColor = MyAppTheme.colors.itemBg
    val targetAnimColor = MyAppTheme.colors.lightBlue1

    scope.launch {
        itemAnimateColor.animateTo(
            targetValue = targetAnimColor,
            animationSpec = tween(Util.EDIT_ITEM_ANIM_TIME)
        )
        itemAnimateColor.animateTo(
            targetValue = baseColor,
            animationSpec = tween(Util.EDIT_ITEM_ANIM_TIME)
        )
    }

    onComplete()
    return Modifier
}

@SuppressLint("ModifierFactoryUnreferencedReceiver", "CoroutineCreationDuringComposition")
@Composable
fun Modifier.removeAnim(
    scope: CoroutineScope,
    onComplete: () -> Unit = {}
): Modifier {

    val scale = remember {
        Animatable(1f)
    }

    scope.launch {
        scale.animateTo(
            targetValue = 0f,
            animationSpec = tween(Util.ADD_ITEM_ANIM_TIME)
        )
    }
    if (scale.value <= 0f) {
        onComplete()
    }
    return Modifier.scale(scale.value)
}