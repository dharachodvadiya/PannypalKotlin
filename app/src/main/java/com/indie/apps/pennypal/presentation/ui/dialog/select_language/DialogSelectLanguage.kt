package com.indie.apps.pennypal.presentation.ui.dialog.select_language

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pennypal.presentation.ui.screen.changeLanguage
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun DialogLanguage(
    viewModel: SelectLanguageViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onSelect: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val currentOptionIndex by viewModel.currentOptionIndex.collectAsStateWithLifecycle()
    val optionList by viewModel.optionList.collectAsStateWithLifecycle()

    MyAppDialog(
        isBackEnable = true,
        title = R.string.select_language,
        onNavigationUp = {
            onNavigationUp()
        },
        content = {
            DialogLanguageData(
                optionList = optionList,
                selectedIndex = currentOptionIndex,
                onSelect = {
                    viewModel.onSaveOption(it) { languageCode ->
                        changeLanguage(context = context, languageCode.asString(context))
                        onSelect()
                    }
                }
            )

        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun MyAppDialogPreview() {
    PennyPalTheme(darkTheme = true) {
        DialogLanguage(
            onNavigationUp = {},
            onSelect = { },
        )
    }
}