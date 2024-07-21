package com.indie.apps.pannypal.presentation.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.ListItem
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

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
        val imageVector = Icons.Default.SouthWest
        val bgColor = MyAppTheme.colors.redBg

        ListItem(
            onClick = onClick,
            leadingIcon = {
                RoundImage(
                    imageVector = imageVector,
                    tint = MyAppTheme.colors.white,
                    backGround = bgColor,
                    contentDescription = "person"
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
fun TopBarProfile(
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


@Preview(showBackground = true)
@Composable
private fun TopbarProfilePreview() {
    PannyPalTheme {
        TopBarProfile(onClick = { })
    }
}

@Preview
@Composable
private fun OverviewListItemPreview() {
    PannyPalTheme {
        OverviewListItem({})
    }
}