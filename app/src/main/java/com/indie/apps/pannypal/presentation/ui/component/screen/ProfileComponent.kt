package com.indie.apps.pannypal.presentation.ui.component.screen

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.presentation.ui.component.UserProfile
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun ProfileTopSection(
    totalAmount : Double = 0.0,
    modifier: Modifier = Modifier
){

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MyAppTheme.colors.brandBg,
                shape = RoundedCornerShape(bottomStartPercent = 20, bottomEndPercent = 20)
            )
    ) {

        UserProfile()
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Balance",
            style = MyAppTheme.typography.Semibold60,
            color = MyAppTheme.colors.black
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = Util.getFormattedStringWithSymbol(totalAmount),
            style = MyAppTheme.typography.Semibold90,
            color = MyAppTheme.colors.black
        )
    }
}

@Composable
fun ProfileSection2(
    incomeAmount: Double = 0.0,
    expenseAmount: Double = 0.0,
    onLoginWithGoogle: () -> Unit,
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
            ProfileAmountWithIcon(
                amount = incomeAmount,
                isPositive = true
            )
            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .height(70.dp)
                    .background(MyAppTheme.colors.gray1)
            )
            ProfileAmountWithIcon(
                amount = -expenseAmount,
                isPositive = false
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        ProfileLoginWithGoogleButton(
            onClick = onLoginWithGoogle,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding))
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.button_height))
        )
    }
}

@Composable
private fun ProfileLoginWithGoogleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    PrimaryButton(
        bgColor = MyAppTheme.colors.white,
        borderStroke = BorderStroke(
            width = 1.dp,
            color = MyAppTheme.colors.brand
        ),
        modifier = modifier,
        onClick = onClick,
    ){
        Image(
            painter = painterResource(id = R.drawable.google),
            contentDescription = "Google"
        )
        Text(
            text = stringResource(id = R.string.login_with_google),
            style = MyAppTheme.typography.Bold49_5,
            color = MyAppTheme.colors.black,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ProfileAmountWithIcon(
    amount: Double,
    isPositive : Boolean,
    modifier: Modifier = Modifier
){

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {

        val imageVector = if(isPositive) Icons.Default.SouthWest else Icons.Default.NorthEast
        val bgColor = if(isPositive)MyAppTheme.colors.greenBg else MyAppTheme.colors.redBg

        RoundImage(
            imageVector = imageVector,
            tint = MyAppTheme.colors.white,
            backGround = bgColor,
            contentDescription = "amount"
        )

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Balance",
            style = MyAppTheme.typography.Medium33,
            color = MyAppTheme.colors.gray2
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = Util.getFormattedStringWithSymbol(amount),
            style = MyAppTheme.typography.Bold52_5,
            color = MyAppTheme.colors.black
        )
    }
}

@Preview
@Composable
private fun ProfileLoginWithGoogleButtonPreview() {
    PannyPalTheme {
        ProfileLoginWithGoogleButton({}, modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding))
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.button_height)))
    }
}

