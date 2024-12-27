package com.indie.apps.pennypal.presentation.ui.dialog.select_language

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.TextWithRadioButton
import com.indie.apps.pennypal.util.AppLanguage

@Composable
fun DialogLanguageData(
    selectedIndex: Int,
    optionList: List<AppLanguage>,
    onSelect: (AppLanguage) -> Unit
) {

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


}
