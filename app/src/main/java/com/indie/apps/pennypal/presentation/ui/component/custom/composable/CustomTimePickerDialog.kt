package com.indie.apps.pennypal.presentation.ui.component.custom.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTimePickerDialog(
    currentTimeInMilli: Long,
    onDismiss: () -> Unit,
    onTimeSelected: (Calendar) -> Unit,
    modifier: Modifier = Modifier
) {
    val initCal = Calendar.getInstance().apply {
        timeInMillis = currentTimeInMilli
    }

    val state = rememberTimePickerState(
        initialHour = initCal.get(Calendar.HOUR_OF_DAY),
        initialMinute = initCal.get(Calendar.MINUTE)
    )

    Dialog(
        onDismissRequest = onDismiss,
        content = {
            Column(
                modifier = modifier
                    .roundedCornerBackground(backgroundColor = MyAppTheme.colors.bottomBg)
                    .padding(dimensionResource(id = R.dimen.padding))
            ) {
                TimePicker(
                    state = state,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = MyAppTheme.colors.itemBg,
                        clockDialSelectedContentColor = MyAppTheme.colors.black,
                        clockDialUnselectedContentColor = MyAppTheme.colors.black,
                        selectorColor = MyAppTheme.colors.lightBlue1,
                        periodSelectorSelectedContainerColor = MyAppTheme.colors.itemSelectedBg,
                        periodSelectorUnselectedContainerColor = MyAppTheme.colors.itemBg,
                        periodSelectorSelectedContentColor = MyAppTheme.colors.black,
                        periodSelectorUnselectedContentColor = MyAppTheme.colors.black,
                        timeSelectorSelectedContainerColor = MyAppTheme.colors.itemSelectedBg,
                        timeSelectorUnselectedContainerColor = MyAppTheme.colors.itemBg,
                        timeSelectorSelectedContentColor = MyAppTheme.colors.black,
                        timeSelectorUnselectedContentColor = MyAppTheme.colors.black
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    //Spacer(modifier = Modifier.weight(1f))
                    PrimaryButton(
                        modifier = Modifier.width(80.dp),
                        onClick = {

                            initCal.set(Calendar.HOUR_OF_DAY, state.hour)
                            initCal.set(Calendar.MINUTE, state.minute)

                            onTimeSelected(initCal)
                        }) {
                        CustomText(
                            stringResource(R.string.select),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding)))

                    PrimaryButton(
                        modifier = Modifier.width(80.dp),
                        onClick = onDismiss
                    ) {
                        CustomText(
                            stringResource(R.string.dismiss),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    )
}