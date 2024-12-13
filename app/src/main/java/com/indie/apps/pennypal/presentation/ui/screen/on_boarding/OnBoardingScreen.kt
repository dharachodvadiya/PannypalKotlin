package com.indie.apps.pennypal.presentation.ui.screen.on_boarding

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.navigation.OnBoardingPage
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun OnBoardingScreen(
    viewModel: OnBoardingViewModel = hiltViewModel(),
    countryCode: String?,
    onCurrencyChange: () -> Unit,
    onBoardingComplete: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val currentPageState by viewModel.currentPageState.collectAsStateWithLifecycle()
    val currencyText by viewModel.currencyText.collectAsStateWithLifecycle()
    val nameState by viewModel.nameState.collectAsStateWithLifecycle()

    LaunchedEffect(countryCode) {
        viewModel.setCountryCode(countryCode ?: viewModel.getDefaultCurrencyCode())
    }

    OnBoardingScreenStart(
        onBoardingPage = currentPageState,
        onClick = {
            viewModel.onContinueClick(it) {
                onBoardingComplete()
            }
        },
        introData = viewModel.introDataList,
        modifier = modifier,
        onBackClick = viewModel::onBackClick,
        onCurrencySelect = {
            onCurrencyChange()
        },
        currencyText = currencyText,
        nameState = nameState,
        onNameTextChange = viewModel::updateNameText
    )
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun OnBoardingScreenStart(
    onBoardingPage: OnBoardingPage,
    onClick: (OnBoardingPage) -> Unit,
    onBackClick: (OnBoardingPage) -> Unit,
    onCurrencySelect: () -> Unit,
    introData: List<IntroData>,
    currencyText: String,
    nameState: TextFieldState,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    onNameTextChange: (String) -> Unit
) {
    Scaffold { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                .padding(innerPadding)
                .padding(horizontal = dimensionResource(id = R.dimen.padding))
        ) {

            when (onBoardingPage) {
                OnBoardingPage.BEGIN -> OnBoardingBeginPage(onClick = { onClick(onBoardingPage) })
                OnBoardingPage.INTRO -> OnBoardingIntroPage(
                    onClick = { onClick(onBoardingPage) },
                    introData = introData,
                    onBackClick = { onBackClick(onBoardingPage) })

                OnBoardingPage.SET_NAME -> OnBoardingSetNamePage(
                    onClick = { onClick(onBoardingPage) },
                    onBackClick = { onBackClick(onBoardingPage) },
                    nameState = nameState,
                    onNameTextChange = onNameTextChange
                )

                OnBoardingPage.SET_CURRENCY -> OnBoardingSetCurrencyPage(
                    onClick = {
                        onClick(
                            onBoardingPage
                        )
                    },
                    onBackClick = { onBackClick(onBoardingPage) },
                    onCurrencySelect = onCurrencySelect,
                    currencyText = currencyText
                )

                OnBoardingPage.WELCOME -> OnBoardingWelcomePage(
                    onClick = { onClick(onBoardingPage) },
                    onBackClick = { onBackClick(onBoardingPage) })

                OnBoardingPage.RESTORE -> OnBoardingRestorePage(onClick = { onClick(onBoardingPage) })
            }
        }
    }

}


@Preview
@Composable
private fun NewItemScreenPreview() {
    PennyPalTheme(darkTheme = true) {
    }
}