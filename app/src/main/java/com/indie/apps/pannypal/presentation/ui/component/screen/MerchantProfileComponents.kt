package com.indie.apps.pannypal.presentation.ui.component.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.presentation.ui.component.UserProfile
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun MerchantProfileTopSection(
    modifier: Modifier = Modifier
){

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
    ) {

        UserProfile(borderWidth = 1f)
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Name",
            style = MyAppTheme.typography.Semibold60,
            color = MyAppTheme.colors.black
        )
        Text(
            text = "description",
            style = MyAppTheme.typography.Medium40,
            color = MyAppTheme.colors.gray2,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(min = 0.dp, max =  200.dp)
        )
    }
}

@Composable
fun MerchantProfileBottomSection(
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
                color = MyAppTheme.colors.gray0
            )
            .padding(dimensionResource(id = R.dimen.padding)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Outlined.Phone, contentDescription ="")
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.item_content_padding)))

        Column {
            Text(
                text = stringResource(id = R.string.phone_number_placeholder),
                style = MyAppTheme.typography.Medium40,
                color = MyAppTheme.colors.gray2
            )
            Text(
                text = "+00 000000000",
                style = MyAppTheme.typography.Semibold50,
                color = MyAppTheme.colors.black
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun MerchantProfileTopSectionPreview() {
    PannyPalTheme {
        MerchantProfileTopSection()
    }
}

@Preview(showBackground = true)
@Composable
private fun MerchantProfileBottomSectionPreview() {
    PannyPalTheme {
        MerchantProfileBottomSection()
    }
}