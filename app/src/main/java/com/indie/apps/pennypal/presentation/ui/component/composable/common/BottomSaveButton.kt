package com.indie.apps.pennypal.presentation.ui.component.composable.common

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomText
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme

@Composable
fun BottomSaveButton(
    @StringRes textId: Int = R.string.save,
    onClick: () -> Unit,
    enabled: Boolean = true,
    @DrawableRes icon: Int? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    PrimaryButton(
        //bgBrush = linearGradientsBrush(MyAppTheme.colors.gradientBlue),
        bgColor = MyAppTheme.colors.buttonBg,
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.button_height)),
        onClick = onClick,
        enabled = enabled
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null)
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "icon",
                    modifier = Modifier.size(25.dp)
                )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding)))
            CustomText(
                text = stringResource(id = textId),
                style = MyAppTheme.typography.Bold49_5,
                color = MyAppTheme.colors.black,
                textAlign = TextAlign.Center,
            )
        }

    }
}