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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomProgressDialog
import com.indie.apps.pennypal.presentation.ui.component.showToast
import com.indie.apps.pennypal.presentation.ui.navigation.OnBoardingPage
import com.indie.apps.pennypal.presentation.ui.screen.AuthViewModel
import com.indie.apps.pennypal.presentation.ui.screen.SignInLauncher
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.AuthProcess
import com.indie.apps.pennypal.util.SyncEvent

@SuppressLint("StateFlowValueCalledInComposition", "RememberReturnType")
@Composable
fun OnBoardingScreen(
    viewModel: OnBoardingViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    countryCode: String?,
    onCurrencyChange: () -> Unit,
    onBoardingComplete: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val currentPageState by viewModel.currentPageState.collectAsStateWithLifecycle()
    val currencyText by viewModel.currencyText.collectAsStateWithLifecycle()
    val nameState by viewModel.nameState.collectAsStateWithLifecycle()
    val isSignInProcess = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(countryCode) {
        viewModel.setCountryCode(countryCode ?: viewModel.getDefaultCurrencyCode())
    }

    val processingState by authViewModel.processingState.collectAsStateWithLifecycle()

    when (processingState) {
        AuthProcess.BACK_UP -> CustomProgressDialog(R.string.backup_Data)
        AuthProcess.RESTORE -> CustomProgressDialog(R.string.restore_Data)
        AuthProcess.NONE -> {}
    }

    if (isSignInProcess.value) {
        CustomProgressDialog(R.string.sign_in)
    }

    val restoreSuccessMessage = stringResource(id = R.string.restore_success)
    val loginSuccessMessage = stringResource(id = R.string.signin_success)

    val context = LocalContext.current
    SignInLauncher(authViewModel,
        onLoginSuccess = {
            authViewModel.isBackupAvailable { isBackUpAvailable ->

                context.showToast(loginSuccessMessage)
                isSignInProcess.value = false
                viewModel.onContinueClick(currentPageState,
                    isBackUpAvailable = isBackUpAvailable,
                    onBoardingComplete = {
                        onBoardingComplete()
                    })
            }

        },
        onRestoreSuccess = {
            context.showToast(restoreSuccessMessage)
            viewModel.onContinueClick(currentPageState,
                onBoardingComplete = {
                    onBoardingComplete()
                })
        },
        onLoginFail = { isSignInProcess.value = false })

    OnBoardingScreenStart(
        onBoardingPage = currentPageState,
        onClick = {
            if (viewModel.isLoginClick(it)) {
                isSignInProcess.value = true
                authViewModel.onEvent(
                    mainEvent = SyncEvent.SignInGoogle
                )
            } else if (viewModel.isRestoreClick(it)) {
                authViewModel.onEvent(
                    mainEvent = SyncEvent.Restore
                )
            } else {
                viewModel.onContinueClick(it, onBoardingComplete = {
                    onBoardingComplete()
                })
            }
        },
        onEndClick = {
            viewModel.onContinueClick(
                currentPageState, onBoardingComplete = {
                    onBoardingComplete()
                },
                isBackUpAvailable = false
            )
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
    onEndClick: () -> Unit,
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
                    onBackClick = { onBackClick(onBoardingPage) },
                    onGuestLoginClick = onEndClick
                )

                OnBoardingPage.RESTORE -> OnBoardingRestorePage(
                    onClick = { onClick(onBoardingPage) },
                    onEndClick = onEndClick
                )
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