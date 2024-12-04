package com.indie.apps.pennypal.presentation.ui.component.custom.composable

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun CustomMonthPickerDialog(
    currentYear: Int,
    currentMonth: Int,
    onDismiss: () -> Unit,
    onDateSelected: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    //val state = rememberDatePickerState(currentTimeInMilli + Util.TIME_ZONE_OFFSET_IN_MILLI)
    val selectedYear by remember { mutableIntStateOf(currentYear) }

    val months = listOf(
        "JAN",
        "FEB",
        "MAR",
        "APR",
        "MAY",
        "JUN",
        "JUL",
        "AUG",
        "SEP",
        "OCT",
        "NOV",
        "DEC"
    )

    var monthIndex by remember {
        mutableIntStateOf(currentMonth)
    }

    var year by remember {
        mutableIntStateOf(currentYear)
    }

    /*val interactionSource = remember {
        MutableInteractionSource()
    }*/

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss,
        content = {
            Column(
                modifier = modifier
                    .padding(dimensionResource(id = R.dimen.padding))
                    .roundedCornerBackground(backgroundColor = MyAppTheme.colors.bottomBg)
                    .padding(dimensionResource(id = R.dimen.padding))

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        modifier = Modifier
                            .size(35.dp)
                            .clickable(
                                interactionSource =  MutableInteractionSource(),
                                indication = null,
                            ) {
                                year--
                            },
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "previous",
                        tint = MyAppTheme.colors.black
                    )

                    CustomText(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        text = year.toString(),
                        color = MyAppTheme.colors.black
                    )

                    Icon(
                        modifier = Modifier
                            .size(35.dp)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null,
                            ) {
                                year++
                            },
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "previous",
                        tint = MyAppTheme.colors.black
                    )

                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(months.size) { index ->
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null,
                                ) {
                                    monthIndex = index
                                }
                                .background(
                                    color = MyAppTheme.colors.transparent
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            val animatedSize by animateDpAsState(
                                targetValue = if (monthIndex == index) 60.dp else 0.dp,
                                animationSpec = tween(
                                    durationMillis = 300,
                                    easing = LinearOutSlowInEasing
                                ), label = ""
                            )

                            Box(
                                modifier = Modifier
                                    .size(animatedSize)
                                    .background(
                                        color = if (monthIndex == index) MyAppTheme.colors.itemSelectedBg else MyAppTheme.colors.transparent,
                                        shape = CircleShape
                                    )
                            )

                            CustomText(
                                text = months[index],
                                color = MyAppTheme.colors.black,
                            )

                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    PrimaryButton(onClick = {
                        onDateSelected(year, monthIndex)
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

@Preview
@Composable
private fun OverViewScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        CustomMonthPickerDialog(
            currentYear = 2025,
            currentMonth = 8,
            onDismiss = {},
            onDateSelected = {_, _ ->}
        )
    }
}
