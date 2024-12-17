package com.indie.apps.pennypal.presentation.ui.component

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal fun Modifier.roundedCornerBackground(
    backgroundColor: Color,
    border: BorderStroke? = null,
    shadowElevation: Dp = 0.dp
) = this
    .shadow(shadowElevation, RoundedCornerShape(10.dp), clip = false)
    .clip(RoundedCornerShape(10.dp))
    .then(if (border != null) Modifier.border(border, RoundedCornerShape(10.dp)) else Modifier)
    .background(color = backgroundColor, shape = RoundedCornerShape(10.dp))

internal fun Intent.setRequestCode(requestCode: Int) {
    this.putExtra("requestCode", requestCode)
}

internal fun Intent.getRequestCode() = this.getIntExtra("requestCode", 0)

internal fun Modifier.clickableWithNoRipple(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
) = this.clickable(
    interactionSource = MutableInteractionSource(),
    indication = null,
    enabled = enabled,
    onClickLabel = onClickLabel,
    role = role,
    onClick = onClick
)
