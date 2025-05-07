package com.indie.apps.pennypal.presentation.ui.screen.payment

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomText
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun PaymentModeDefaultItem(
    paymentModeName: String,
    onDefaultPaymentChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .roundedCornerBackground(MyAppTheme.colors.itemBg)
            .clickableWithNoRipple { onDefaultPaymentChange() }
            /* .background(
                 shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
                 color = MyAppTheme.colors.itemBg
             )*/
            .padding(dimensionResource(id = R.dimen.padding)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            CustomText(
                text = stringResource(id = R.string.default_payment_mode),
                style = MyAppTheme.typography.Medium40,
                color = MyAppTheme.colors.gray2
            )
            CustomText(
                text = paymentModeName,
                style = MyAppTheme.typography.Semibold50,
                color = MyAppTheme.colors.black
            )
        }


        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "edit",
            tint = MyAppTheme.colors.black,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}

@Composable
fun AccountHeadingItem(
    @StringRes title: Int,
    modifier: Modifier = Modifier
) {
    CustomText(
        text = stringResource(id = title),
        style = MyAppTheme.typography.Regular51,
        color = MyAppTheme.colors.gray1,
        modifier = modifier
    )
}

@Preview
@Composable
private fun PaymentModeDefaultItemPreview() {
    PennyPalTheme(darkTheme = true) {
        PaymentModeDefaultItem(
            paymentModeName = "cash",
            onDefaultPaymentChange = {}
        )
    }
}
