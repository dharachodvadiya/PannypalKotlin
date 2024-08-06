package com.indie.apps.pannypal.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.data.entity.User
import com.indie.apps.pannypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pannypal.presentation.ui.component.screen.ProfileSection2
import com.indie.apps.pannypal.presentation.ui.component.screen.ProfileTopSection
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme
import com.indie.apps.pannypal.presentation.viewmodel.ProfileViewModel
import com.indie.apps.pannypal.util.Resource

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit
) {
    val uiState by profileViewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is Resource.Loading -> {
            LoadingScreen()
        }

        is Resource.Success -> {
            uiState.data?.let {
                ProfileScreenData(
                    onNavigationUp = onNavigationUp,
                    user = it
                )
            }
        }

        is Resource.Error -> {}
    }
}

@Composable
private fun ProfileScreenData(
    onNavigationUp: () -> Unit,
    user: User
) {
    Scaffold(
        topBar = {
            TopBarWithTitle(
                title = stringResource(id = R.string.profile),
                onNavigationUp = onNavigationUp,
                contentAlignment = Alignment.Center,
                bgColor = MyAppTheme.colors.brandBg
            )
        }
    ) { padding ->

        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(padding)
        ) {
            ProfileTopSection(
                totalAmount = (user.incomeAmount - user.expenseAmount),
                modifier = Modifier.height(screenHeight * 0.3f)
            )
            ProfileSection2(
                incomeAmount = user.incomeAmount,
                expenseAmount = user.expenseAmount,
                onLoginWithGoogle = {})
        }

    }
}


@Preview
@Composable
private fun ProfileScreenPreview() {
    PannyPalTheme {
        ProfileScreen(onNavigationUp = {})
    }
}