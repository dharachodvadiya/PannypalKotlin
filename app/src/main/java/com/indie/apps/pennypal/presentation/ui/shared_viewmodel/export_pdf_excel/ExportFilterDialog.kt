package com.indie.apps.pennypal.presentation.ui.shared_viewmodel.export_pdf_excel

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.composable.common.DialogSelectableItem
import com.indie.apps.pennypal.presentation.ui.component.composable.common.TextFieldError
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomDatePickerDialog
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomText
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.app_enum.DialogType
import com.indie.apps.pennypal.util.app_enum.ErrorMessage
import com.indie.apps.pennypal.util.internanal.method.getDateFromMillis
import java.text.SimpleDateFormat
import java.util.Calendar

@SuppressLint("SimpleDateFormat")
@Composable
fun ExportFilterDialog(
    @StringRes title: Int,
    onDismiss: () -> Unit,
    onExportClick: (startDate: Long, endDate: Long) -> Unit
) {
    val context = LocalContext.current
    var startDateMilli by remember {
        mutableLongStateOf(
            Calendar.getInstance().apply { add(Calendar.MONTH, -6) }.timeInMillis
        )
    }
    var endDateMilli by remember { mutableLongStateOf(Calendar.getInstance().timeInMillis) }
    var errorText by remember { mutableStateOf<String>("") }
    var openDialog by remember { mutableStateOf<DialogType?>(null) }



    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            CustomText(
                text = stringResource(title),
                style = MyAppTheme.typography.Semibold57,
                color = MyAppTheme.colors.black
            )
        },
        text = {
            val dateFormat = SimpleDateFormat("dd MMM yyyy")
            Column {
                DialogSelectableItem(
                    label = R.string.from,
                    text = getDateFromMillis(startDateMilli, dateFormat),
                    onClick = {
                        openDialog = DialogType.FromDate
                    },
                    placeholder = R.string.from,
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "calender",
                            tint = MyAppTheme.colors.gray1,
                            modifier = Modifier
                                .roundedCornerBackground(MyAppTheme.colors.transparent)
                                .clickableWithNoRipple {
                                    openDialog = DialogType.FromDate
                                })
                    },
                    errorText = "",
                )

                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding)))

                DialogSelectableItem(
                    label = R.string.to,
                    onClick = {
                        openDialog = DialogType.ToDate
                    },
                    text = getDateFromMillis(endDateMilli, dateFormat),
                    placeholder = R.string.to,
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "calender",
                            tint = MyAppTheme.colors.gray1,
                            modifier = Modifier
                                .roundedCornerBackground(MyAppTheme.colors.transparent)
                                .clickableWithNoRipple {
                                    openDialog = DialogType.ToDate
                                })
                    },
                    errorText = ""
                )

                TextFieldError(
                    textError = errorText
                )

            }
        },
        confirmButton = {
            TextButton(onClick = {
                when {
                    startDateMilli > endDateMilli -> {
                        errorText = ErrorMessage.INCORRECT_DATE.toString()
                    }

                    !isUnder6Month(startDateMilli, endDateMilli) -> {
                        errorText = ErrorMessage.EXCEED_DATE_PERIOD.toString()
                    }

                    else -> {
                        onExportClick(startDateMilli, endDateMilli)
                        onDismiss()
                    }
                }
            }) {
                CustomText(stringResource(R.string.export))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                CustomText(stringResource(R.string.cancel))
            }
        }
    )

    openDialog?.let { dialog ->
        when (dialog) {
            DialogType.FromDate -> {
                CustomDatePickerDialog(
                    currentTimeInMilli = if (startDateMilli == 0L) Calendar.getInstance().timeInMillis else startDateMilli,
                    onDateSelected = {
                        openDialog = null
                        startDateMilli = it.timeInMillis
                    },
                    onDismiss = {
                        openDialog = null
                    }
                )
            }

            DialogType.ToDate -> {
                CustomDatePickerDialog(
                    currentTimeInMilli = if (endDateMilli == 0L) Calendar.getInstance().timeInMillis else endDateMilli,
                    onDateSelected = {
                        openDialog = null
                        endDateMilli = it.timeInMillis
                    },
                    onDismiss = {
                        openDialog = null
                    }
                )
            }

            else -> {}
        }
    }
}

private fun isUnder6Month(startDateMilli: Long, endDateMilli: Long): Boolean {
    val startCal = Calendar.getInstance().apply { timeInMillis = startDateMilli }
    val endCal = Calendar.getInstance().apply { timeInMillis = endDateMilli }

    val yearDiff = endCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR)
    val monthDiff = endCal.get(Calendar.MONTH) - startCal.get(Calendar.MONTH)

    val totalMonthDiff = yearDiff * 12 + monthDiff

    return totalMonthDiff <= 6 && !endCal.before(startCal)
}