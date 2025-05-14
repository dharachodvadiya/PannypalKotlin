package com.indie.apps.pennypal.presentation.ui.screen.purchase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.purchase.ProductUiModel
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomText
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun PurchaseItem(
    productUiModel: ProductUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val offerDetails = productUiModel.productDetails.subscriptionOfferDetails?.firstOrNull()
    val pricingPhase = offerDetails
        ?.pricingPhases
        ?.pricingPhaseList
        ?.firstOrNull()

    val price = pricingPhase?.formattedPrice ?: "N/A"
    val billingPeriod = pricingPhase?.billingPeriod ?: "N/A" // e.g. P1M

    val durationNum = when {
        billingPeriod.contains("P1W") -> "1"
        billingPeriod.contains("P1M") -> "1"
        billingPeriod.contains("P3M") -> "3"
        billingPeriod.contains("P6M") -> "6"
        billingPeriod.contains("P1Y") -> "1"
        else -> "1"
    }
    val durationText = when {
        billingPeriod.contains("P1W") -> "Week"
        billingPeriod.contains("P1M") -> "Month"
        billingPeriod.contains("P3M") -> "Months"
        billingPeriod.contains("P6M") -> "Months"
        billingPeriod.contains("P1Y") -> "Year"
        else -> "Subscription"
    }



    Column(
        modifier = modifier
            .width(150.dp)
            .height(200.dp)
            .roundedCornerBackground(MyAppTheme.colors.brand)
            .padding(5.dp)
            .roundedCornerBackground(MyAppTheme.colors.itemSelectedBg)
            .clickableWithNoRipple { onClick() }
            .padding(dimensionResource(R.dimen.item_inner_padding)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (productUiModel.isPurchased) {
            Box(
                modifier = Modifier
                    .height(25.dp)
                    .width(100.dp)
                    .roundedCornerBackground(MyAppTheme.colors.greenBg)
                    .padding(5.dp)
            ) {

                CustomText(
                    text = stringResource(R.string.active_now),
                    color = MyAppTheme.colors.white,
                    modifier = Modifier.fillMaxSize(),
                    style = MyAppTheme.typography.Medium34,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Spacer(Modifier.height(25.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = dimensionResource(R.dimen.item_inner_padding)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            CustomText(
                text = durationNum,
                color = MyAppTheme.colors.black,
                style = MyAppTheme.typography.Semibold90,
                textAlign = TextAlign.Center
            )
            CustomText(
                text = durationText,
                color = MyAppTheme.colors.gray0,
                modifier = Modifier,
                style = MyAppTheme.typography.Regular51,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.weight(1f))

            CustomText(
                text = productUiModel.productDetails.name,
                color = MyAppTheme.colors.gray0,
                modifier = Modifier,
                style = MyAppTheme.typography.Regular51,
                textAlign = TextAlign.Center
            )
            CustomText(
                text = price,
                color = MyAppTheme.colors.black,
                style = MyAppTheme.typography.Semibold60,
                textAlign = TextAlign.Center
            )
        }


    }
}

@Preview
@Composable
private fun NewItemScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        /* OnBoardingBeginPage(
             onClick = {},
         )*/
    }
}