package com.indie.apps.pennypal.presentation.ui.component.custom.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
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

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss,
        content = {
            Column(
                modifier = modifier
                    .padding(dimensionResource(id = R.dimen.padding))
                    .roundedCornerBackground(backgroundColor = MyAppTheme.colors.bottomBg)

            ) {
                DatePicker(state = state)

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
                    PrimaryButton(onClick = {
                        if (state.selectedDateMillis != null) {
                            val selectedCalender = Calendar.getInstance().apply() {
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
                        CustomText("Select")
                    }

                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding)))

                    PrimaryButton(onClick = onDismiss) {
                        CustomText("Cancel")
                    }
                }
            }


        }
    )


}
