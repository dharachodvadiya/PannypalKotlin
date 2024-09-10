package com.indie.apps.pennypal.presentation.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.entity.User
import com.indie.apps.pennypal.presentation.ui.component.showToast
import com.indie.apps.pennypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.screen.loading.LoadingWithProgress
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    code: String?,
    onCurrencyChangeClick: () -> Unit
) {
    val context = LocalContext.current
    val userUpdateToast = stringResource(id = R.string.user_update_success_message)

    val userData by profileViewModel.currUserData.collectAsStateWithLifecycle()

    if (userData == null) {
        LoadingWithProgress()
    } else {
        LaunchedEffect(key1 = Unit) {
            profileViewModel.setCountryCode(code)
        }

        userData?.let {
            ProfileScreenData(
                onNavigationUp = onNavigationUp,
                user = it,
                symbol = profileViewModel.getSymbolFromCurrencyCode(it.currency),
                isSaveEnable = profileViewModel.getIsSavable(),
                onCurrencyChangeClick = onCurrencyChangeClick,
                onSaveClick = {
                    profileViewModel.saveUser { isSuccess ->
                        if (isSuccess) {
                            context.showToast(userUpdateToast)
                            onNavigationUp()
                        }
                    }
                }
            )
        }
    }


}

@Composable
private fun ProfileScreenData(
    onNavigationUp: () -> Unit,
    user: User,
    symbol: String,
    onCurrencyChangeClick: () -> Unit,
    onSaveClick: (User) -> Unit,
    isSaveEnable: Boolean = false
) {

    Scaffold(
        topBar = {
            TopBarWithTitle(
                title = stringResource(id = R.string.profile),
                onNavigationUp = onNavigationUp,
                contentAlignment = Alignment.Center,
                bgColor = MyAppTheme.colors.itemBg,
                trailingContent = {
                    if (isSaveEnable) {
                        Icon(
                            Icons.Default.Done,
                            contentDescription = "save",
                            modifier = Modifier
                                .roundedCornerBackground(MyAppTheme.colors.bottomBg)
                                .clickable {
                                    onSaveClick(user)
                                })
                    }

                }
            )
        }
    ) { padding ->

        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(MyAppTheme.colors.brandBg)
                .padding(padding)
        ) {
            ProfileTopSection(
                symbol = symbol,
                totalAmount = (user.incomeAmount - user.expenseAmount),
                modifier = Modifier.height(screenHeight * 0.3f)
            )
            ProfileSection2(
                user = user,
                onLoginWithGoogle = {},
                onCurrencyChangeClick = onCurrencyChangeClick,
                symbol = symbol
            )
        }

    }
}


@Preview
@Composable
private fun ProfileScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        ProfileScreen(onNavigationUp = {}, onCurrencyChangeClick = {}, code = "US")
    }
}