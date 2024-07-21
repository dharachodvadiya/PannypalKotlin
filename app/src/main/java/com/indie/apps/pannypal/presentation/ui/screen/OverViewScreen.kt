package com.indie.apps.pannypal.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.presentation.ui.component.OverviewListItem
import com.indie.apps.pannypal.presentation.ui.component.PrimaryButton
import com.indie.apps.pannypal.presentation.ui.component.TopBarProfile
import com.indie.apps.pannypal.presentation.ui.component.VerticalGradientsColor
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun OverViewScreen(
    onProfileClick : ()-> Unit,
    onNewEntry : () -> Unit,
    modifier: Modifier = Modifier) {

    Scaffold(
        topBar = {
            OverViewTopBar(
                onSearchTextChange = {},
                onProfileClick = onProfileClick
            )
        },
        floatingActionButton = {
            AppFloatingButton(onClick = onNewEntry)
        }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .padding(innerPadding)
        ) {
            BalanceView(-10000.0)
            OverviewList()
        }

    }
}

@Composable
fun AppFloatingButton(
    onClick : () -> Unit,
    modifier: Modifier = Modifier
){
    PrimaryButton(
        onClick = onClick,
        modifier = modifier
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "New Entry")
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = stringResource(R.string.new_entry),
                style = MyAppTheme.typography.Medium45_29
            )
        }
    }
}

@Composable
fun OverViewTopBar(
    onSearchTextChange: (String)-> Unit,
    onProfileClick : ()-> Unit,
    modifier: Modifier = Modifier
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.top_bar))
            .padding(horizontal = dimensionResource(R.dimen.padding))
    ) {
        var isSearch by remember { mutableStateOf(false) }

        if(!isSearch)
            TopBarProfile(onClick = onProfileClick)

        if(isSearch)
            CustomSearchView(
                onTextChange = onSearchTextChange,
                onBackClick = {
                    isSearch = false
                },
                modifier = Modifier
                    .weight(1f)
            )

        if(!isSearch){
            Spacer(modifier = Modifier.weight(1f))

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

    }
}

@Composable
fun BalanceView(
    balance : Double,
    modifier: Modifier = Modifier
){

    val colorStroke = if(balance>=0) MyAppTheme.colors.greenBg else MyAppTheme.colors.redBg
    val colorGradient = if(balance>=0) MyAppTheme.colors.gradientGreen else MyAppTheme.colors.gradientRed
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(brush = VerticalGradientsColor(colorGradient))
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
){
    LazyColumn(
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.padding))
    ){
        items(10) { index ->

            OverviewListItem(
                onClick = { /*TODO*/ },
                isDateShow = (index % 3 == 0))
        }
    }
}

@Composable
fun CustomSearchView(
    onTextChange: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var textState by remember { mutableStateOf("") }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(R.dimen.round_corner)))
    ) {
        TextField(
            value = textState,
            onValueChange = {
                textState = it
                onTextChange(textState)
            },
            colors = TextFieldDefaults.colors(
                cursorColor = MyAppTheme.colors.brand,
                focusedIndicatorColor = MyAppTheme.colors.transparent,
                unfocusedIndicatorColor = MyAppTheme.colors.transparent,
                disabledIndicatorColor = MyAppTheme.colors.transparent,
                focusedContainerColor = MyAppTheme.colors.transparent,
                unfocusedContainerColor = MyAppTheme.colors.transparent
            ),
            placeholder = {
                Text(text = "Search",
                    style = MyAppTheme.typography.Regular57,
                    color = MyAppTheme.colors.gray2)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            //keyboardActions = KeyboardActions(onSearch = { onSearchClick(text) }),
            leadingIcon = {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.clickable {
                        if (textState.isNotEmpty()) {
                            textState = ""
                        }else{
                            onBackClick()
                        }
                    }
                )
            },
            trailingIcon = {
                if (textState.isNotEmpty()) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (textState.isNotEmpty()) {
                                textState = ""
                            }
                        },
                        imageVector = Icons.Filled.Close,
                        contentDescription = "close"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MyAppTheme.colors.white),
            textStyle = MyAppTheme.typography.Medium56
        )



    }
}

@Preview
@Composable
private fun OverViewScreenPreview() {
    PannyPalTheme {
        OverViewScreen({},{})
    }
}