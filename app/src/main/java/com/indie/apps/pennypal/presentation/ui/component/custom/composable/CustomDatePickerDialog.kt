package com.indie.apps.pennypal.presentation.ui.component.custom.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.Util
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    currentTimeInMilli: Long,
    onDismiss: () -> Unit,
    onDateSelected: (Calendar) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberDatePickerState(currentTimeInMilli + Util.TIME_ZONE_OFFSET_IN_MILLI)
    var isInitialLoad by remember { mutableStateOf(true) } // Track if it's the first composition

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss,
        content = {
            Column(
                modifier = modifier
                    .padding(dimensionResource(id = R.dimen.padding))
                    .roundedCornerBackground(backgroundColor = MyAppTheme.colors.bottomBg)

            ) {
                val colors = DatePickerDefaults.colors().copy(
                    containerColor = MyAppTheme.colors.bottomBg,
                    selectedDayContentColor = MyAppTheme.colors.black

                )
                DatePicker(
                    state = state,
                    colors = colors
                )

                // Observe date selection and trigger callback only on user change
                LaunchedEffect(state.selectedDateMillis) {
                    if (state.selectedDateMillis != null) {
                        if (isInitialLoad) {
                            isInitialLoad = false // Skip the initial load
                        } else {
                            val selectedCalendar = Calendar.getInstance().apply {
                                timeInMillis = state.selectedDateMillis!!
                            }
                            val calendar = Calendar.getInstance().apply {
                                timeInMillis = currentTimeInMilli
                                set(Calendar.YEAR, selectedCalendar.get(Calendar.YEAR))
                                set(Calendar.MONTH, selectedCalendar.get(Calendar.MONTH))
                                set(
                                    Calendar.DAY_OF_MONTH,
                                    selectedCalendar.get(Calendar.DAY_OF_MONTH)
                                )
                            }
                            onDateSelected(calendar)
                            onDismiss() // Close dialog after user selection
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            PaddingValues(
                                end = dimensionResource(id = R.dimen.padding),
                                bottom = dimensionResource(id = R.dimen.padding)
                            )
                        ),
                    horizontalArrangement = Arrangement.End
                ) {
                    /*PrimaryButton(
                        modifier = Modifier.width(80.dp),
                        onClick = {
                            if (state.selectedDateMillis != null) {
                                val selectedCalender = Calendar.getInstance().apply {
                                    timeInMillis = state.selectedDateMillis!!
                                }

                                val calendar = Calendar.getInstance().apply {
                                    timeInMillis = currentTimeInMilli
                                    set(Calendar.YEAR, selectedCalender.get(Calendar.YEAR))
                                    set(Calendar.MONTH, selectedCalender.get(Calendar.MONTH))
                                    set(
                                        Calendar.DAY_OF_MONTH,
                                        selectedCalender.get(Calendar.DAY_OF_MONTH)
                                    )
                                }
                                onDateSelected(calendar)
                            }
                        }) {
                        CustomText(
                            stringResource(R.string.select),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding)))*/

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
