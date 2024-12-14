package com.indie.apps.pennypal.presentation.ui.screen.on_boarding

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pennypal.presentation.ui.component.DialogSelectableItem
import com.indie.apps.pennypal.presentation.ui.component.DialogTextFieldItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun OnBoardingBeginPage(
    onClick: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    OnBoardingPage(
        onClick = onClick,
        buttonText = R.string.lets_begin,
        content = {
        },
        isBackEnable = false,
        modifier = modifier
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingIntroPage(
    onClick: () -> Unit,
    onBackClick: () -> Unit,
    introData: List<IntroData>,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    OnBoardingPage(
        onClick = onClick,
        buttonText = R.string.continue_text,
        content = {
            // Define a pager state
            val pagerState = rememberPagerState()

            /*// CoroutineScope to launch auto-scroll feature
            val scope = rememberCoroutineScope()

            LaunchedEffect(true) {
                println("aaaaa 111")
                while (true) {
                    println("aaaaa 222")
                    delay(2000) // auto scroll every 3 seconds
                    if (pagerState.currentPage < pages.size - 1) {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    } else {
                        pagerState.animateScrollToPage(0) // loop back to the first page
                    }
                    println("aaaaa 333")
                }
            }*/

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    indicatorWidth = 25.dp,
                    indicatorHeight = 5.dp,
                    spacing = 5.dp,
                    activeColor = MyAppTheme.colors.black,
                    inactiveColor = MyAppTheme.colors.gray1.copy(alpha = 0.5f)
                )

                HorizontalPager(
                    count = introData.size,
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { pageIndex ->
                    IntroPageContent(
                        introData = introData[pageIndex]
                    )
                }
            }


        },
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
fun OnBoardingSetNamePage(
    nameState: TextFieldState,
    onClick: () -> Unit,
    onBackClick: () -> Unit,
    onNameTextChange: (String) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    OnBoardingPage(
        onClick = onClick,
        buttonText = R.string.continue_text,
        content = {

            Column {
                CustomText(
                    text = stringResource(id = R.string.intro_name_title),
                    color = MyAppTheme.colors.black,
                    modifier = Modifier
                        .padding(16.dp),
                    style = MyAppTheme.typography.Regular57
                )

                DialogTextFieldItem(
                    textState = nameState,
                    placeholder = R.string.name,
                    textTrailingContent = {
                    },
                    onTextChange = { onNameTextChange(it) },
                    modifier = Modifier
                        .padding(top = dimensionResource(id = R.dimen.padding))
                )
            }


        },
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
fun OnBoardingSetCurrencyPage(
    onClick: () -> Unit,
    onBackClick: () -> Unit,
    onCurrencySelect: () -> Unit,
    currencyText: String,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    OnBoardingPage(
        onClick = onClick,
        buttonText = R.string.continue_text,
        content = {

            Column {
                CustomText(
                    text = stringResource(id = R.string.intro_currency_title),
                    color = MyAppTheme.colors.black,
                    modifier = Modifier
                        .padding(16.dp),
                    style = MyAppTheme.typography.Regular57
                )

                DialogSelectableItem(
                    text = currencyText,
                    onClick = onCurrencySelect,
                    placeholder = R.string.add_payment_type_placeholder,
                    isSelectable = true,
                    errorText = "",
                    trailingContent = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Add",
                            tint = MyAppTheme.colors.gray1
                        )
                    }
                )
            }


        },
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
fun OnBoardingWelcomePage(
    onClick: () -> Unit,
    onBackClick: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    OnBoardingPage(
        onClick = onClick,
        buttonText = R.string.login_with_google,
        content = {
        },
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
fun OnBoardingRestorePage(
    onClick: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    OnBoardingPage(
        onClick = onClick,
        buttonText = R.string.restore_Data,
        content = {
        },
        isBackEnable = false,
        modifier = modifier
    )
}


@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
private fun OnBoardingPage(
    onClick: () -> Unit,
    onBackClick: () -> Unit = {},
    buttonText: Int,
    isBackEnable: Boolean = true,
    content: @Composable () -> Unit = {},
    bottomContent: @Composable () -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        if (isBackEnable) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .roundedCornerBackground(MyAppTheme.colors.transparent)
                        .clickable {
                            onBackClick()
                        },
                    tint = MyAppTheme.colors.black
                )
            }
        }
        Box(modifier = Modifier.weight(1f)) {
            content()
        }
        BottomSaveButton(
            textId = buttonText,
            onClick = onClick,
        )
        bottomContent()
        Spacer(modifier = Modifier.height(60.dp))
    }
}


@Composable
private fun IntroPageContent(introData: IntroData) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            CustomText(
                text = stringResource(id = introData.title),
                color = MyAppTheme.colors.black,
                modifier = Modifier.padding(16.dp),
                style = MyAppTheme.typography.Semibold90
            )
            CustomText(
                text = stringResource(id = introData.subTitle),
                color = MyAppTheme.colors.black,
                modifier = Modifier.padding(16.dp),
                style = MyAppTheme.typography.Regular51
            )
        }
    }
}

@Preview
@Composable
private fun NewItemScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        OnBoardingPage(
            onClick = {},
            buttonText = R.string.save,
            onBackClick = {},
        )
    }
}