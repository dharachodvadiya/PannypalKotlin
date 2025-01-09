package com.indie.apps.pennypal.presentation.ui.screen.on_boarding

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import com.indie.apps.pennypal.presentation.ui.component.TextWithRadioButton
import com.indie.apps.pennypal.presentation.ui.component.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.AppLanguage

@Composable
fun OnBoardingBeginPage(
    onClick: () -> Unit, @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    OnBoardingPage(
        onClick = onClick, buttonText = R.string.lets_begin, content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(painter = painterResource(id = R.drawable.intro), contentDescription = "img")
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CustomText(
                        text = stringResource(id = R.string.begin_title),
                        color = MyAppTheme.colors.black,
                        modifier = Modifier.padding(16.dp),
                        style = MyAppTheme.typography.Semibold90,
                        textAlign = TextAlign.Center
                    )
                    CustomText(
                        text = stringResource(id = R.string.begin_subtitle),
                        color = MyAppTheme.colors.gray1,
                        modifier = Modifier
                            .width(320.dp)
                            .padding(16.dp),
                        style = MyAppTheme.typography.Regular51,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }, isBackEnable = false, modifier = modifier
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
        onClick = onClick, buttonText = R.string.continue_text, content = {
            // Define a pager state
            val pagerState = rememberPagerState()

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
                    count = introData.size, state = pagerState, modifier = Modifier.fillMaxSize()
                ) { pageIndex ->
                    IntroPageContent(
                        introData = introData[pageIndex]
                    )
                }
            }


        }, onBackClick = onBackClick, modifier = modifier
    )
}

@Composable
fun OnBoardingSetLanguagePage(
    onClick: () -> Unit,
    onBackClick: () -> Unit,
    optionList: List<AppLanguage>,
    selectedIndex: Int,
    onSelect: (AppLanguage) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    OnBoardingPage(
        onClick = onClick, buttonText = R.string.continue_text, content = {

            Column {
                CustomText(
                    text = stringResource(id = R.string.select_language),
                    color = MyAppTheme.colors.black,
                    modifier = Modifier.padding(16.dp),
                    style = MyAppTheme.typography.Regular57
                )

                Column(
                    modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding))
                ) {
                    optionList.forEach { item ->
                        TextWithRadioButton(
                            isSelected = item.index == selectedIndex,
                            name = stringResource(item.title),
                            onSelect = { onSelect(item) },
                        )

                    }

                    Spacer(modifier = Modifier.height(50.dp))
                }

                /*DialogTextFieldItem(
                    textState = nameState,
                    placeholder = R.string.name,
                    textTrailingContent = {},
                    onTextChange = { onNameTextChange(it) },
                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding))
                )*/
            }


        }, onBackClick = onBackClick, modifier = modifier
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
        onClick = onClick, buttonText = R.string.continue_text, content = {

            Column {
                CustomText(
                    text = stringResource(id = R.string.intro_name_title),
                    color = MyAppTheme.colors.black,
                    modifier = Modifier.padding(16.dp),
                    style = MyAppTheme.typography.Regular57
                )

                DialogTextFieldItem(
                    textState = nameState,
                    placeholder = R.string.name,
                    textTrailingContent = {},
                    onTextChange = { onNameTextChange(it) },
                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding))
                )
            }


        }, onBackClick = onBackClick, modifier = modifier
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
        onClick = onClick, buttonText = R.string.continue_text, content = {

            Column {
                CustomText(
                    text = stringResource(id = R.string.intro_currency_title),
                    color = MyAppTheme.colors.black,
                    modifier = Modifier.padding(16.dp),
                    style = MyAppTheme.typography.Regular57
                )

                DialogSelectableItem(text = currencyText,
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
                    })
            }


        }, onBackClick = onBackClick, modifier = modifier
    )
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun OnBoardingWelcomePage(
    onClick: () -> Unit,
    onGuestLoginClick: () -> Unit,
    onBackClick: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    OnBoardingPage(
        onClick = onClick, buttonText = R.string.login_with_google, content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    CustomText(
                        text = stringResource(id = R.string.welcome_title),
                        color = MyAppTheme.colors.black,
                        style = MyAppTheme.typography.Semibold90,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomText(
                        text = stringResource(id = R.string.welcome_subtitle),
                        color = MyAppTheme.colors.gray1,
                        style = MyAppTheme.typography.Regular57,
                    )
                }
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.all_done),
                        contentDescription = "img",
                        modifier = Modifier.size(200.dp)
                    )
                }
            }
        },
        onBackClick = onBackClick,
        buttonIcon = R.drawable.google,
        bottomContent = {
            Box(modifier = Modifier
                .fillMaxWidth()
                .clickableWithNoRipple(
                    //   interactionSource = MutableInteractionSource(), indication = null
                ) { onGuestLoginClick() }
                .padding(dimensionResource(id = R.dimen.padding)),
                contentAlignment = Alignment.Center) {
                CustomText(
                    text = stringResource(id = R.string.start_without_login),
                    color = MyAppTheme.colors.gray1,
                    style = MyAppTheme.typography.Regular46,
                )
            }
        },
        modifier = modifier
    )
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun OnBoardingRestorePage(
    onClick: () -> Unit,
    onEndClick: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    OnBoardingPage(
        onClick = onClick,
        buttonText = R.string.restore_Data,
        content = {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painter = painterResource(id = R.drawable.restore_data),
                    contentDescription = "img",
                    modifier = Modifier
                        .size(300.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    CustomText(
                        text = stringResource(R.string.restore_title),
                        color = MyAppTheme.colors.black,
                        style = MyAppTheme.typography.Semibold90,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomText(
                        text = stringResource(R.string.restore_subtitle),
                        color = MyAppTheme.colors.gray1,
                        style = MyAppTheme.typography.Regular57,
                    )
                }
            }
        },
        isBackEnable = false,
        bottomContent = {
            Box(modifier = Modifier
                .fillMaxWidth()
                .clickableWithNoRipple(
                    //   interactionSource = MutableInteractionSource(), indication = null
                ) {
                    onEndClick()
                }
                .padding(dimensionResource(id = R.dimen.padding)),
                contentAlignment = Alignment.Center) {
                CustomText(
                    text = "Skip",
                    color = MyAppTheme.colors.gray1,
                    style = MyAppTheme.typography.Regular46,
                )
            }
        },
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
    @DrawableRes buttonIcon: Int? = null,
    content: @Composable () -> Unit = {},
    bottomContent: @Composable () -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        if (isBackEnable) {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .roundedCornerBackground(MyAppTheme.colors.transparent)
                        .clickableWithNoRipple {
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
            textId = buttonText, onClick = onClick, icon = buttonIcon
        )
        bottomContent()
        Spacer(modifier = Modifier.height(60.dp))
    }
}


@Composable
private fun IntroPageContent(introData: IntroData) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .rotate(45f)
                .background(color = MyAppTheme.colors.lightBlue2, shape = RoundedCornerShape(20.dp))
                .padding(bottom = 2.5.dp, end = 2.5.dp)
                .shadow(5.dp, RoundedCornerShape(20.dp), clip = false),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = introData.imageId),
                contentDescription = "img",
                modifier = Modifier
                    .size(90.dp)
                    .rotate(-45f)
            )
        }


        Column(modifier = Modifier.fillMaxWidth()) {
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
        /* OnBoardingBeginPage(
             onClick = {},
         )*/
    }
}