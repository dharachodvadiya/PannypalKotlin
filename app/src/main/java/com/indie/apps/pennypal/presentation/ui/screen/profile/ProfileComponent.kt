package com.indie.apps.pennypal.presentation.ui.screen.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.entity.User
import com.indie.apps.pennypal.presentation.ui.component.UserProfileRound
import com.indie.apps.pennypal.presentation.ui.component.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.AutoSizeText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.Util

@Composable
fun ProfileTopSection(
    symbol: String,
    totalAmount: Double = 0.0,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MyAppTheme.colors.itemBg,
                shape = RoundedCornerShape(bottomStartPercent = 20, bottomEndPercent = 20)
            )
    ) {

        UserProfileRound()
        Spacer(modifier = Modifier.height(20.dp))
        CustomText(
            text = stringResource(id = R.string.balance),
            style = MyAppTheme.typography.Semibold60,
            color = MyAppTheme.colors.gray1
        )
        Spacer(modifier = Modifier.height(5.dp))
        /*CustomText(
            text = Util.getFormattedStringWithSymbol(totalAmount),
            style = MyAppTheme.typography.Semibold90,
            color = MyAppTheme.colors.black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = TextUnit.Unspecified,
            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding))
        )*/

        AutoSizeText(
            text = Util.getFormattedStringWithSymbol(totalAmount, symbol),
            style = MyAppTheme.typography.Regular66_5,
            color = MyAppTheme.colors.black,
            alignment = Alignment.Center,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding))
        )
    }
}

@Composable
fun ProfileSection2(
    user: User,
    symbol: String,
    onLoginWithGoogle: () -> Unit,
    onCurrencyChangeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            /*ProfileAmountWithIcon(
                currencySymbol = symbol,
                amount = user.incomeAmount,
                isPositive = true,
                modifier = Modifier.width(150.dp)
            )
            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .height(70.dp)
                    .background(MyAppTheme.colors.gray1)
            )
            ProfileAmountWithIcon(
                currencySymbol = symbol,
                amount = user.expenseAmount,
                isPositive = false,
                modifier = Modifier.width(150.dp)
            )*/
        }

        /*ProfileCurrencyItem(
            currencyCode = user.currency,
            currencySymbol = symbol,
            onClick = onCurrencyChangeClick
        )

        Spacer(modifier = Modifier.weight(1f))*/

        /* ProfileLoginWithGoogleButton(
             onClick = onLoginWithGoogle,
             modifier = Modifier
                 .padding(dimensionResource(id = R.dimen.padding))
                 .fillMaxWidth()
                 .height(dimensionResource(id = R.dimen.button_height))
         )*/
    }
}

@Composable
private fun ProfileLoginWithGoogleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    PrimaryButton(
        bgColor = MyAppTheme.colors.buttonBg,
        borderStroke = BorderStroke(
            width = 1.dp,
            color = MyAppTheme.colors.brand
        ),
        modifier = modifier,
        onClick = onClick,
    ) {
        Image(
            painter = painterResource(id = R.drawable.google),
            contentDescription = "Google"
        )
        CustomText(
            text = stringResource(id = R.string.login_with_google),
            style = MyAppTheme.typography.Bold49_5,
            color = MyAppTheme.colors.black,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ProfileCurrencyItem(
    currencyCode: String,
    currencySymbol: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.padding))
            .roundedCornerBackground(MyAppTheme.colors.bottomBg)
            .clickableWithNoRipple(role = Role.Button) { onClick() }
            /*.background(
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
                color = MyAppTheme.colors.bottomBg
            )*/
            .padding(dimensionResource(id = R.dimen.padding)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            CustomText(
                text = stringResource(id = R.string.currency_and_format),
                style = MyAppTheme.typography.Medium40,
                color = MyAppTheme.colors.gray2
            )
            CustomText(
                text = "$currencyCode ($currencySymbol)",
                style = MyAppTheme.typography.Semibold50,
                color = MyAppTheme.colors.black
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.NavigateNext,
            contentDescription = "",
            tint = MyAppTheme.colors.gray1
        )
    }
}

@Composable
private fun ProfileAmountWithIcon(
    currencySymbol: String,
    amount: Double,
    isPositive: Boolean,
    modifier: Modifier = Modifier
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {

        val imageVector = if (isPositive) Icons.Default.SouthWest else Icons.Default.NorthEast
        val bgColor = if (isPositive) MyAppTheme.colors.greenBg else MyAppTheme.colors.redBg

        RoundImage(
            imageVector = imageVector,
            tint = MyAppTheme.colors.black,
            backGround = bgColor,
            contentDescription = "amount"
        )

        Spacer(modifier = Modifier.height(10.dp))
        CustomText(
            text = "Balance",
            style = MyAppTheme.typography.Medium33,
            color = MyAppTheme.colors.gray2
        )
        Spacer(modifier = Modifier.height(5.dp))
        AutoSizeText(
            text = Util.getFormattedStringWithSymbol(amount, currencySymbol),
            //minTextSize = 10.sp,
            style = MyAppTheme.typography.Regular51,
            color = MyAppTheme.colors.black,
            alignment = Alignment.Center,
            maxLines = 2
        )
    }
}

@Preview
@Composable
private fun ProfileLoginWithGoogleButtonPreview() {
    /* PennyPalTheme(darkTheme = true) {
         *//*ProfileLoginWithGoogleButton(
            {}, modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding))
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.button_height))
        )*//*

        ProfileSection2(onLoginWithGoogle = { *//*TODO*//* })
    }*/
}

