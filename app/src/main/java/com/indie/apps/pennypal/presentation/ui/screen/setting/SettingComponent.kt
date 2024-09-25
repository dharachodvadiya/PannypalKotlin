package com.indie.apps.pennypal.presentation.ui.screen.setting

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.MoreItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.screen.payment.AccountHeadingItem
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme


@Composable
fun SettingTypeItem(
    @StringRes titleId: Int,
    dataList: List<MoreItem>,
    onSelect: (MoreItem) -> Unit = {},
    arrowIconEnable: Boolean,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.padding)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding))
    ) {
        AccountHeadingItem(titleId)
        Column(
            modifier = modifier
                .fillMaxWidth()
                .roundedCornerBackground(MyAppTheme.colors.itemBg, shadowElevation = 5.dp)
                /*.background(
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
                    color = MyAppTheme.colors.itemBg
                )*/
                .padding(dimensionResource(id = R.dimen.padding))
        ) {

            dataList.forEachIndexed { index, item ->
                SettingItem(
                    data = item,
                    onClick = {
                        onSelect(item)
                    },
                    showDivider = index != dataList.size - 1,
                    arrowIconEnable = arrowIconEnable
                )
            }
        }
    }
}

@Composable
fun SettingItem(
    data: MoreItem,
    onClick: () -> Unit,
    showDivider: Boolean = true,
    arrowIconEnable: Boolean,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .roundedCornerBackground(MyAppTheme.colors.transparent)
            .clickable { onClick() }
            /*.background(
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
                color = MyAppTheme.colors.bottomBg
            )*/
            .padding(dimensionResource(id = R.dimen.item_inner_padding)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (data.icon != null) {
            Icon(
                painter = painterResource(id = data.icon),
                contentDescription = "",
                tint = MyAppTheme.colors.gray1
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding)))
        }
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            CustomText(
                text = stringResource(id = data.title),
                style = MyAppTheme.typography.Semibold50,
                color = MyAppTheme.colors.black
            )
            if (!data.subTitle.isNullOrEmpty()) {
                CustomText(
                    text = data.subTitle,
                    style = MyAppTheme.typography.Medium40,
                    color = MyAppTheme.colors.gray2
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        if (arrowIconEnable) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                contentDescription = "",
                tint = MyAppTheme.colors.gray1
            )
        }
    }

    if (showDivider)
        Divider(color = MyAppTheme.colors.gray3.copy(alpha = 0.5f))
}

fun onShareClick(context: Context) {
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            "Check out this awesome app! \n https://play.google.com/store/apps/details?id=${context.packageName}&hl=en"
        )
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(intent, "Share via"))
}

fun onRateClick(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${context.packageName}"))
    context.startActivity(intent)
}

fun onPrivacyPolicyClick(context: Context) {
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://sites.google.com/view/privacypolicyforpennypal/home")
    )
    context.startActivity(intent)
}

@SuppressLint("IntentReset")
fun onContactUsClick(context: Context) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:videostatus.group@gmail.com")
        putExtra(Intent.EXTRA_SUBJECT, "Penny Pal Contact Us")
        putExtra(Intent.EXTRA_TEXT, "Hello, I have a question about...")
    }
    try {
        context.startActivity(Intent.createChooser(intent, "Send mail"))
    } catch (e: ActivityNotFoundException) {

    }

}