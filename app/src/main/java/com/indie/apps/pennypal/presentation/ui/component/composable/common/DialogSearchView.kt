package com.indie.apps.pennypal.presentation.ui.component.composable.common

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.SearchView
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme

@Composable
fun DialogSearchView(
    searchState: TextFieldState,
    onTextChange: (String) -> Unit,
    focusRequesterSearch: FocusRequester? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = modifier.padding(
            horizontal = dimensionResource(id = R.dimen.padding), vertical = 7.dp
        )
    ) {
        SearchView(
            trailingIcon = Icons.Default.Search,
            textState = searchState,
            bgColor = MyAppTheme.colors.lightBlue2,
            modifier = Modifier
                .weight(1f)
                .height(40.dp),
            paddingValues = PaddingValues(
                top = 0.dp,
                bottom = 0.dp,
                start = dimensionResource(id = R.dimen.padding),
                end = 0.dp
            ),
            onTextChange = onTextChange,
            focusRequesterSearch = focusRequesterSearch
        )
        Spacer(modifier = Modifier.width(5.dp))
        if (trailingContent != null) {
            trailingContent()
        }
    }
}