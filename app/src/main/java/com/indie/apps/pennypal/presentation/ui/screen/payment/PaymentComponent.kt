package com.indie.apps.pennypal.presentation.ui.screen.payment

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme

@Composable
fun PaymentModeDefaultItem(
    paymentModeName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.padding))
            .roundedCornerBackground(MyAppTheme.colors.itemBg)
            /* .background(
                 shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
                 color = MyAppTheme.colors.itemBg
             )*/
            .padding(dimensionResource(id = R.dimen.padding))
    ) {
        Text(
            text = stringResource(id = R.string.default_payment_mode),
            style = MyAppTheme.typography.Medium40,
            color = MyAppTheme.colors.gray2
        )
        Text(
            text = paymentModeName,
            style = MyAppTheme.typography.Semibold50,
            color = MyAppTheme.colors.black
        )
    }
}

@Composable
fun AccountHeadingItem(
    @StringRes title: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = title),
        style = MyAppTheme.typography.Regular51,
        color = MyAppTheme.colors.gray1,
        modifier = modifier
    )
}
