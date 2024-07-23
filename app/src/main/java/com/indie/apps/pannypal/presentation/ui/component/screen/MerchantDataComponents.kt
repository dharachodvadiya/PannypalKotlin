package com.indie.apps.pannypal.presentation.ui.component.screen

import android.icu.util.CurrencyAmount
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.ListItem
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.SearchView
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.TopBar
import com.indie.apps.pannypal.presentation.ui.component.linearGradientsBrush
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun MerchantDataTopBar(
    onClick: () -> Unit,
    onNavigationUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopBar(
        isBackEnable = true,
        onBackClick = onNavigationUp,
        content = {
            MerchantDataTopBarItem(
                onClick = onClick
            )
        },
        modifier = modifier
    )
}

@Composable
private fun MerchantDataTopBarItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    val imageVector = Icons.Default.Person
    val bgColor = MyAppTheme.colors.brandBg


    ListItem(
        onClick = onClick,
        leadingIcon = {
            RoundImage(
                imageVector = imageVector,
                brush = linearGradientsBrush(MyAppTheme.colors.gradientBlue),
                backGround = bgColor,
                contentDescription = "person",
                modifier = Modifier.size(50.dp),
                imageVectorSize = 27.dp
            )
        },
        content = {
            Column {
                Text(
                    text = "Name",
                    style = MyAppTheme.typography.Semibold56,
                    color = MyAppTheme.colors.black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Description",
                    style = MyAppTheme.typography.Medium44,
                    color = MyAppTheme.colors.gray2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun MerchantDataBottomBar(
    isEditable: Boolean = false,
    isDeletable: Boolean = false,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    if (isEditable || isDeletable) {
        Row(
            modifier = modifier.padding(dimensionResource(id = R.dimen.padding))
        ) {
            if (isEditable) {
                MerchantDataBottomButton(
                    text = R.string.edit_transaction,
                    imageVector = Icons.Default.Edit,
                    bgBrush = linearGradientsBrush(MyAppTheme.colors.gradientBlue),
                    onClick = onEditClick,
                    modifier = Modifier.weight(1f)
                )
            }

            if (isDeletable) {
                Spacer(modifier = Modifier.width(7.dp))
                MerchantDataBottomButton(
                    text = R.string.delete_transaction,
                    imageVector = Icons.Default.DeleteOutline,
                    bgColor = MyAppTheme.colors.redBg,
                    onClick = onDeleteClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }

    } else {
        MerchantDataBottomTotal(
            modifier = modifier
                .padding(top = 7.dp,
                    bottom = 0.dp,
                    start = 0.dp,
                    end = 0.dp))
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MerchantDataIncomeAmount(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
                role = Role.Button
            )
    ) {

        MerchantDataAmountItem(
            amount = 50.0,
            description = "fdsdas fsFD dzfdss fsdfsFS fSfdfadsf",
            contentAlignment = Alignment.Start,
            colorStroke = MyAppTheme.colors.greenBg
        )

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MerchantDataExpenseAmount(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
                role = Role.Button
            ),
        horizontalArrangement = Arrangement.End
    ) {

        MerchantDataAmountItem(
            amount = -50.0,
            description = "dfsdfs fssaa dasdasdadfsdfsdasdasadzXZ",
            contentAlignment = Alignment.End,
            colorStroke = MyAppTheme.colors.redBg
        )

    }
}

@Composable
private fun MerchantDataAmountItem(
    amount: Double,
    description: String? = null,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment.Horizontal,
    colorStroke: Color = MyAppTheme.colors.black
) {
    Surface(
        modifier = modifier
            .widthIn(min = 150.dp, max = 250.dp),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
        color = MyAppTheme.colors.gray0
    )
    {
        Column(
            modifier = modifier
                .drawBehind {
                    drawLine(
                        colorStroke,
                        Offset(0f, size.height),
                        Offset(size.width, size.height),
                        25f
                    )
                }
                .padding(5.dp)
                .padding(horizontal = 10.dp),
            horizontalAlignment = contentAlignment
        ) {
            Text(
                text = Util.getFormattedStringWithSymbole(amount),
                style = MyAppTheme.typography.Semibold80,
                color = MyAppTheme.colors.black
            )
            if (!description.isNullOrEmpty()) {
                Text(
                    text = description,
                    style = MyAppTheme.typography.Medium46,
                    color = MyAppTheme.colors.gray2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
    }
}

@Composable
private fun MerchantDataBottomButton(
    @StringRes text: Int,
    imageVector: ImageVector,
    bgBrush: Brush? = null,
    bgColor: Color = MyAppTheme.colors.brand,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PrimaryButton(
        bgColor = bgColor,
        bgBrush = bgBrush,
        modifier = modifier
            .height(dimensionResource(id = R.dimen.button_height)),
        onClick = onClick,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "",
            tint = MyAppTheme.colors.white
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = stringResource(text),
            style = MyAppTheme.typography.Bold49_5,
            color = MyAppTheme.colors.white,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun MerchantDataBottomTotal(
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(69.dp)
            .background(
                shape = RoundedCornerShape(
                    topStart = dimensionResource(id = R.dimen.round_corner),
                    topEnd = dimensionResource(id = R.dimen.round_corner),
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                ),
                color = MyAppTheme.colors.gray0
            )
            .padding(horizontal = dimensionResource(id = R.dimen.padding)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MerchantDataTotalIncomeExpense(amount = 5.0, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(7.dp))
        MerchantDataTotalIncomeExpense(amount = -7.0, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(7.dp))
        MerchantDataTotal(amount = 10.0, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun MerchantDataTotalIncomeExpense(
    amount: Double = 0.0,
    modifier: Modifier = Modifier
) {
    val strokeColor = if (amount >= 0) MyAppTheme.colors.greenBg else MyAppTheme.colors.redBg

    Box(
        modifier = modifier
            .height(dimensionResource(id = R.dimen.top_bar_profile))
            .background(
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
                color = MyAppTheme.colors.white
            )
            .border(
                width = 2.dp,
                color = strokeColor,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = Util.getFormattedStringWithSymbole(0.0),
            style = MyAppTheme.typography.Semibold50,
            color = MyAppTheme.colors.black
        )
    }
}

@Composable
private fun MerchantDataTotal(
    amount: Double = 0.0,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = Util.getFormattedStringWithSymbole(0.0),
            style = MyAppTheme.typography.Semibold56,
            color = MyAppTheme.colors.black,
        )
        Text(
            text = stringResource(id = R.string.total_amount),
            style = MyAppTheme.typography.Medium40,
            color = MyAppTheme.colors.gray2
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MerchantDataTopBarPreview() {
    PannyPalTheme {
        MerchantDataTopBar(onClick = { /*TODO*/ }, onNavigationUp = { /*TODO*/ })
    }
}

@Preview(showBackground = true)
@Composable
private fun MerchantDataBottomBarPreview() {
    PannyPalTheme {
        MerchantDataBottomBar(
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MerchantDataIncomeAmountItemPreview() {
    PannyPalTheme {
        MerchantDataIncomeAmount(
            onClick = {},
            onLongClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MerchantDataExpenseAmountItemPreview() {
    PannyPalTheme {
        MerchantDataExpenseAmount(
            onClick = {},
            onLongClick = {}
        )
    }
}