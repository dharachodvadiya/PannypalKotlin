package com.indie.apps.pannypal.presentation.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.component.PrimaryButton
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OverViewScreen(
    onNewEntry : () -> Unit,
    modifier: Modifier = Modifier) {
    Scaffold(
        floatingActionButton = {
            AppFloatingButton(onClick = onNewEntry)
        }
    ){

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