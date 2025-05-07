package com.indie.apps.pennypal.presentation.ui.screen.setting

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.db_entity.Category
import com.indie.apps.pennypal.data.database.db_entity.User
import com.indie.apps.pennypal.data.module.MoreItem
import com.indie.apps.pennypal.presentation.ui.component.composable.common.UserProfileRect
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomText
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.ListItem
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.RoundImage
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.screen.payment.AccountHeadingItem
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.internanal.method.getCategoryIconById
import com.indie.apps.pennypal.util.internanal.method.getDateFromMillis
import com.indie.apps.pennypal.util.internanal.method.getTimeFromMillis
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingItemList(
    dataList: List<MoreItem>,
    onSelect: (MoreItem) -> Unit = {},
    arrowIconEnable: Boolean,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    FlowRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = 2 // Ensure two items per row
    ) {
        dataList.forEachIndexed { index, item ->

            ListItem(
                isClickable = true,
                onClick = { onSelect(item) },
                leadingIcon = {
                    if (item.icon != null) {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = "",
                            tint = MyAppTheme.colors.gray1,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                content = {

                    CustomText(
                        text = stringResource(id = item.title),
                        style = MyAppTheme.typography.Semibold50,
                        color = MyAppTheme.colors.black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                isSetDivider = false,
                modifier = modifier
                    .fillMaxWidth(0.5f)
                    .padding(horizontal = 5.dp),
                itemBgColor = MyAppTheme.colors.itemBg,
                paddingValues = PaddingValues(vertical = 10.dp, horizontal = 5.dp)
            )
        }


    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun SettingItem(
    data: MoreItem,
    onClick: () -> Unit,
    showDivider: Boolean = true,
    arrowIconEnable: Boolean,
    bgColor: Color = MyAppTheme.colors.transparent,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .roundedCornerBackground(bgColor)
            .clickableWithNoRipple(
                //    interactionSource = MutableInteractionSource(),
                //   indication = null,
            ) { onClick() }
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
                tint = MyAppTheme.colors.gray1,
                modifier = Modifier.size(20.dp)
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
            data.subTitle?.let {
                CustomText(
                    text = it.asString(),
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
        HorizontalDivider(color = MyAppTheme.colors.gray3.copy(alpha = 0.5f))
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
    val intent = Intent(Intent.ACTION_VIEW, "market://details?id=${context.packageName}".toUri())
    context.startActivity(intent)
}

fun onPrivacyPolicyClick(context: Context) {
    val intent = Intent(
        Intent.ACTION_VIEW,
        "https://sites.google.com/view/privacypolicyforpennypal/home".toUri()
    )
    context.startActivity(intent)
}

@SuppressLint("IntentReset")
fun onContactUsClick(context: Context) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:videostatus.group@gmail.com".toUri()
        putExtra(Intent.EXTRA_SUBJECT, "Penny Pal Contact Us")
        putExtra(Intent.EXTRA_TEXT, "Hello, I have a question about...")
    }
    try {
        context.startActivity(Intent.createChooser(intent, "Send mail"))
    } catch (_: ActivityNotFoundException) {

    }

}

@SuppressLint("SimpleDateFormat", "UnrememberedMutableInteractionSource")
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
            modifier = Modifier.clickableWithNoRipple(
                //    interactionSource = MutableInteractionSource(),
                //   indication = null,
            ) { onClick() }
        ) {
            /*PrimaryButton(
                bgColor = MyAppTheme.colors.white,
                borderStroke = BorderStroke(
                    width = 1.dp,
                    color = MyAppTheme.colors.gray1
                ),
                onClick = {},
                enabled = false,
                modifier = Modifier.size(dimensionResource(R.dimen.top_bar_profile))
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile",
                    tint = MyAppTheme.colors.gray1
                )
            }*/
            UserProfileRect()

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
        HorizontalDivider()
    }


}


@Composable
fun SettingScreenItem(
    item: Category,
    onClick: () -> Unit,
    itemBgColor: Color,
    leadingIconSize: Dp = 40.dp,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxWidth()
) {
    ListItem(
        isClickable = true,
        onClick = onClick,
        leadingIcon = {

            RoundImage(
                imageVector = ImageVector.vectorResource(
                    getCategoryIconById(
                        item.iconId,
                        LocalContext.current
                    )
                ),
                imageVectorSize = 20.dp,
                tint = MyAppTheme.colors.black,
                backGround = MyAppTheme.colors.transparent,
                modifier = Modifier.size(leadingIconSize),
                contentDescription = item.name
            )
        },
        content = {
            CustomText(
                text = item.name,
                style = MyAppTheme.typography.Semibold52_5,
                color = MyAppTheme.colors.black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        isSetDivider = false,
        modifier = modifier.padding(vertical = 5.dp),
        itemBgColor = itemBgColor
    )
}

