package com.indie.apps.pennypal.presentation.ui.screen.purchase

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.composable.common.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomText
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.linearGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.extension.showToast
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.ProductId

@SuppressLint("StateFlowValueCalledInComposition", "SimpleDateFormat")
@Composable
fun PurchaseScreen(
    viewModel: PurchaseViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val productList = viewModel.productUiList

    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopBarWithTitle(
                title = "",
                onNavigationUp = onNavigationUp,
                contentAlignment = Alignment.Center,
            )
        }
    ) { innerPadding ->

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(linearGradientsBrush(MyAppTheme.colors.gradientBlue))
                .padding(innerPadding),
        ) {

            Image(
                painter = painterResource(id = R.drawable.shape),
                contentDescription = "shape",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )

            Column(
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding))
            ) {
                CustomText(
                    text = stringResource(id = R.string.get_premium_access),
                    color = MyAppTheme.colors.black,
                    style = MyAppTheme.typography.Semibold130,
                    modifier = Modifier.width(200.dp)
                )
                CustomText(
                    text = stringResource(id = R.string.premium_subtitle),
                    color = MyAppTheme.colors.gray0,
                    modifier = Modifier,
                    style = MyAppTheme.typography.Regular57
                )
                Spacer(modifier = Modifier.weight(0.7f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    productList.forEach() { item ->
                        PurchaseItem(item, onClick = {})
                    }


                }
                Spacer(modifier = Modifier.weight(0.5f))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(R.dimen.padding)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomText(
                        text = stringResource(R.string.start_plan_now),
                        color = MyAppTheme.colors.black,
                        style = MyAppTheme.typography.Semibold54,
                        textAlign = TextAlign.Center
                    )
                    CustomText(
                        text = stringResource(R.string.auti_renewal_cancel_anytime),
                        color = MyAppTheme.colors.gray1,
                        modifier = Modifier,
                        style = MyAppTheme.typography.Regular51,
                        textAlign = TextAlign.Center
                    )
                }

                PrimaryButton(
                    //bgBrush = linearGradientsBrush(MyAppTheme.colors.gradientBlue),
                    bgColor = Color(0xFF407A41),
                    modifier = modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.button_height)),
                    onClick = {
                        viewModel.buy(context as Activity, ProductId.NoAds)
                    }
                ) {

                    CustomText(
                        text = stringResource(id = R.string.unlock_now),
                        style = MyAppTheme.typography.Bold49_5,
                        color = MyAppTheme.colors.black,
                        textAlign = TextAlign.Center,
                    )

                }

                Spacer(modifier = Modifier.weight(0.3f))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                            .padding(vertical = dimensionResource(R.dimen.padding)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomText(
                        text = stringResource(R.string.upgrade_experience_today),
                        color = MyAppTheme.colors.gray1,
                        style = MyAppTheme.typography.Regular46,
                        textAlign = TextAlign.Center
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomText(
                            text = stringResource(R.string.restore),
                            color = MyAppTheme.colors.lightBlue1,
                            modifier = Modifier.clickableWithNoRipple {
                                viewModel.restorePurchases() {
                                    context.showToast(it)
                                }
                            },
                            style = MyAppTheme.typography.Regular51,
                            textAlign = TextAlign.Center
                        )

                        Box(
                            modifier
                                .height(20.dp)
                                .width(2.dp)
                                .background(MyAppTheme.colors.gray1)
                        )

                        CustomText(
                            text = stringResource(R.string.manage_subscription),
                            color = MyAppTheme.colors.lightBlue1,
                            modifier = Modifier.clickableWithNoRipple {
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data =
                                    "https://play.google.com/store/account/subscriptions".toUri()
                                context.startActivity(intent)
                            },
                            style = MyAppTheme.typography.Regular51,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

        }


    }

}

@Preview
@Composable
private fun OverViewScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        PurchaseScreen(
            onNavigationUp = {}
        )
    }
}