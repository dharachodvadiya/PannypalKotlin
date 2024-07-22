package com.indie.apps.pannypal.presentation.ui.component.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.presentation.ui.component.verticalGradientsBrush
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.ListItem
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.SearchView
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.TopBar
import com.indie.apps.pannypal.presentation.ui.component.linearGradientsBrush
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun OverviewTopBar(
    onSearchTextChange: (String) -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isSearch by remember { mutableStateOf(false) }

    TopBar(
        isBackEnable = isSearch,
        onBackClick = { isSearch = false },
        leadingContent = { if (!isSearch) OverviewTopBarProfile(onClick = onProfileClick) },
        content = {
            if (isSearch)
                SearchView(
                    onTextChange = onSearchTextChange
                )
        },
        trailingContent = {
            if (!isSearch) {
                IconButton(onClick = {
                    isSearch = true
                }) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Profile",
                        tint = MyAppTheme.colors.black
                    )
                }
            }
        },
        modifier = modifier
    )
}
@Composable
fun OverviewBalanceView(
    balance: Double,
    modifier: Modifier = Modifier
) {

    val colorStroke = if (balance >= 0) MyAppTheme.colors.greenBg else MyAppTheme.colors.redBg
    val colorGradient =
        if (balance >= 0) MyAppTheme.colors.gradientGreen else MyAppTheme.colors.gradientRed
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(brush = verticalGradientsBrush(colorGradient))
            .padding(dimensionResource(id = R.dimen.padding))
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
            color = MyAppTheme.colors.white,
            shadowElevation = dimensionResource(id = R.dimen.shadow_elevation)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawLine(
                            colorStroke,
                            Offset(0f, 0f),
                            Offset(size.width, 0f),
                            25f
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.balance),
                        style = MyAppTheme.typography.Semibold67_5,
                        color = MyAppTheme.colors.gray3
                    )
                    Text(
                        text = Util.getFormattedStringWithSymbole(balance),
                        style = MyAppTheme.typography.Semibold97_5,
                        color = MyAppTheme.colors.black
                    )
                }
            }

        }
    }


}

@Composable
fun OverviewList(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.padding))
    ) {
        items(10) { index ->

            OverviewListItem(
                onClick = {},
                isDateShow = (index % 3 == 0)
            )
        }
    }
}

@Composable
fun OverviewListItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    isDateShow: Boolean = false
) {
    Column(modifier = modifier.background(MyAppTheme.colors.white)) {
        if (isDateShow) {
            // TODO: change total color based on amount sum
            val totalTextColor = MyAppTheme.colors.redBg

            Text(
                text = "Day",
                style = MyAppTheme.typography.Semibold45,
                color = MyAppTheme.colors.gray2
            )
            Text(
                text = Util.getFormattedStringWithSymbole(0.0),
                style = MyAppTheme.typography.Semibold52_5,
                color = totalTextColor,
                modifier = Modifier.padding(vertical = 5.dp)
            )
        }

        // TODO: change Round Image Icon and bg color based on amount
        val imageVector = Icons.Default.NorthEast
        val bgColor = MyAppTheme.colors.redBg

        ListItem(
            onClick = onClick,
            leadingIcon = {
                RoundImage(
                    imageVector = imageVector,
                    tint = MyAppTheme.colors.white,
                    backGround = bgColor,
                    contentDescription = "amount"
                )
            },
            content = {
                Column {
                    Text(
                        text = "Name",
                        style = MyAppTheme.typography.Bold52_5,
                        color = MyAppTheme.colors.black
                    )
                    Text(
                        text = "Description",
                        style = MyAppTheme.typography.Medium33,
                        color = MyAppTheme.colors.gray2
                    )
                }
            },
            trailingContent = {
                Text(
                    text = Util.getFormattedStringWithSymbole(0.0),
                    style = MyAppTheme.typography.Bold52_5,
                    color = MyAppTheme.colors.black
                )
            },
            isSetDivider = true
        )
    }
}

@Composable
fun OverviewTopBarProfile(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .semantics { role = Role.Button },
        shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner)),
        contentColor = MyAppTheme.colors.gray1
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(dimensionResource(R.dimen.top_bar_profile))
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        MyAppTheme.colors.gray1
                    ),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner))
                )
                .background(MyAppTheme.colors.white)
        ) {
            Icon(
                Icons.Filled.Person,
                contentDescription = "Profile"
            )
        }


    }
}

@Composable
fun OverviewAppFloatingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PrimaryButton(
        bgBrush = linearGradientsBrush(MyAppTheme.colors.gradientBlue),
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "New Entry",
                tint = MyAppTheme.colors.white
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = stringResource(R.string.new_entry),
                style = MyAppTheme.typography.Medium45_29,
                color = MyAppTheme.colors.white
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopbarProfilePreview() {
    PannyPalTheme {
        OverviewTopBarProfile(onClick = { })
    }
}

@Preview
@Composable
private fun OverviewListItemPreview() {
    PannyPalTheme {
        OverviewListItem({})
    }
}