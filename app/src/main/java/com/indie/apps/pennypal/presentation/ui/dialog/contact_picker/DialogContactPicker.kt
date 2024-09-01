package com.indie.apps.pennypal.presentation.ui.dialog.contact_picker

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.contacts.data.model.Contact
import com.indie.apps.cpp.data.model.Country
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Util
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("RememberReturnType")
@Composable
fun DialogContactPicker(
    viewModel: ContactPickerViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onSelect: (Contact) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val searchTextState by viewModel.searchTextState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var job: Job? = null

    MyAppDialog(
        isBackEnable = true,
        title = R.string.select_contact,
        onNavigationUp = onNavigationUp,
        content = {
            ContactPickerDialogField(
                onSelect = onSelect,
                searchState = searchTextState,
                contactUiState = uiState,
                onTextChange = {
                    job?.cancel()
                    job = MainScope().launch {
                        delay(Util.SEARCH_NEWS_TIME_DELAY)
                        viewModel.searchData()
                    }
                }
            )
        },
        modifier = modifier,
        isFixHeight = true
    )
}

@Preview
@Composable
private fun DialogCppPreview() {
    PennyPalTheme(darkTheme = true) {
        //DialogCpp({},{})
    }
}