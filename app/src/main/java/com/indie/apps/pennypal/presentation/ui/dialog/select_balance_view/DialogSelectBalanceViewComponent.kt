package com.indie.apps.pennypal.presentation.ui.dialog.select_balance_view

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
import com.indie.apps.pennypal.presentation.ui.component.composable.common.TextWithRadioButton
import com.indie.apps.pennypal.util.app_enum.ShowDataPeriod

@Composable
fun DialogSelectBalanceViewData(
    selectedIndex: Int,
    optionList: List<ShowDataPeriod>,
    onSelect: (ShowDataPeriod) -> Unit
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
