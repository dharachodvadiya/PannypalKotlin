package com.indie.apps.pennypal.presentation.ui.screen.setting

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Person
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
import com.indie.apps.pennypal.data.database.entity.User
import com.indie.apps.pennypal.data.module.MoreItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.screen.payment.AccountHeadingItem
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.getDateFromMillis
import com.indie.apps.pennypal.util.getTimeFromMillis
import java.text.SimpleDateFormat


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
            .fillMaxWidth(),
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
    } catch (_: ActivityNotFoundException) {

    }

}

@SuppressLint("SimpleDateFormat")
@Composable
fun SettingProfileItem(
    user: User?,
    onClick: () -> Unit,
    onBackup: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onClick() }
        ) {
            PrimaryButton(
                bgColor = MyAppTheme.colors.white,
                borderStroke = BorderStroke(
                    width = 1.dp,
                    color = MyAppTheme.colors.gray1
                ),
                onClick = {},
                modifier = Modifier.size(dimensionResource(R.dimen.top_bar_profile))
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile",
                    tint = MyAppTheme.colors.gray1
                )
            }

            Spacer(modifier = Modifier.width(15.dp))

            Column {
                CustomText(
                    text = user?.name ?: "",
                    style = MyAppTheme.typography.Regular57,
                    color = MyAppTheme.colors.black
                )

                CustomText(
                    text = user?.email ?: stringResource(id = R.string.sign_in),
                    style = MyAppTheme.typography.Regular44,
                    color = MyAppTheme.colors.gray0
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val dateFormat = SimpleDateFormat("dd MMM yyyy")
            val timeFormat = SimpleDateFormat("hh:mm aa")

            val str = if (user?.lastSyncDateInMilli != 0L) "${
                user?.let {
                    getDateFromMillis(
                        it.lastSyncDateInMilli,
                        dateFormat
                    )
                }
            } ${
                user?.let {
                    getTimeFromMillis(
                        it.lastSyncDateInMilli,
                        timeFormat
                    )
                }
            }" else stringResource(id = R.string.no_backup_created)

            CustomText(
                text = stringResource(id = R.string.last_backup) + " ",
                style = MyAppTheme.typography.Medium40,
                color = MyAppTheme.colors.gray1.copy(alpha = 0.7f)
            )

            CustomText(
                text = str,
                style = MyAppTheme.typography.Medium40,
                color = MyAppTheme.colors.gray1.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                bgColor = MyAppTheme.colors.transparent,
                onClick = onBackup
            ) {
                CustomText(
                    text = stringResource(id = R.string.backup_now),
                    style = MyAppTheme.typography.Bold49_5,
                    color = MyAppTheme.colors.gray0
                )
            }
        }
        Divider()
    }


}

