package com.indie.apps.pannypal.presentation.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.component.screen.MerchantListItem
import com.indie.apps.pannypal.presentation.ui.component.screen.MerchantTopBar
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun MerchantScreen(
    onMerchantClick:() -> Unit,
    onAddClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditable by remember { mutableStateOf(false) }
    var isDeletable by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            MerchantTopBar(
                isEditable = isEditable,
                isDeletable = isDeletable,
                onAddClick = onAddClick,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
                onNavigationUp = {
                    isDeletable = false
                    isEditable = false
                },
                onSearchTextChange = {}
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = modifier
                .padding(innerPadding)
        ) {
            items(15) { index ->

                MerchantListItem(
                    onClick = {
                        if(!isEditable && !isDeletable)
                        {
                            onMerchantClick()
                        }else{
                            //TODO
                            //isDeletable = !isDeletable
                            // isEditable = !isEditable
                        }

                    },
                    onLongClick = {
                        //TODO
                        //isDeletable = !isDeletable
                        //isEditable = !isEditable
                    }
                )
            }
        }


    }
}

@Preview
@Composable
private fun MerchantScreenPreview() {
    PannyPalTheme {
        MerchantScreen({}, {}, {},{})
    }
}