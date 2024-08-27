package com.indie.apps.pennypal.presentation.ui.screen

import android.widget.Toast
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
import com.indie.apps.cpp.data.utils.getSymbolFromCurrencyCode
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.entity.User
import com.indie.apps.pennypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.component.screen.ProfileSection2
import com.indie.apps.pennypal.presentation.ui.component.screen.ProfileTopSection
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.presentation.viewmodel.ProfileViewModel

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
        LoadingScreen()
    } else {
        profileViewModel.setCurrency(code)
        userData?.let {
            ProfileScreenData(
                onNavigationUp = onNavigationUp,
                user = it,
                isSaveEnable = profileViewModel.getIsSavable(),
                onCurrencyChangeClick = onCurrencyChangeClick,
                onSaveClick = {
                    profileViewModel.saveUser { isSuccess ->
                        if (isSuccess) {
                            Toast.makeText(context, userUpdateToast, Toast.LENGTH_SHORT).show()
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
                            modifier = Modifier.clickable {
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
                symbol = getSymbolFromCurrencyCode(user.currency),
                totalAmount = (user.incomeAmount - user.expenseAmount),
                modifier = Modifier.height(screenHeight * 0.3f)
            )
            ProfileSection2(
                user = user,
                onLoginWithGoogle = {},
                onCurrencyChangeClick = onCurrencyChangeClick
            )
        }

    }
}


@Preview
@Composable
private fun ProfileScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        ProfileScreen(onNavigationUp = {}, onCurrencyChangeClick = {}, code = "USD")
    }
}