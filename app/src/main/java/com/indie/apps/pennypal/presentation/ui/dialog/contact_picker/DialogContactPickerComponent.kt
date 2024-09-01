package com.indie.apps.pennypal.presentation.ui.dialog.contact_picker

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.indie.apps.contacts.data.model.Contact
import com.indie.apps.cpp.data.model.Country
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.DialogSearchView
import com.indie.apps.pennypal.presentation.ui.component.NoDataMessage
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.ListItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.Resource

@Composable
fun ContactPickerDialogField(
    onSelect: (Contact) -> Unit,
    searchState: TextFieldState,
    contactUiState: Resource<List<Contact>>,
    onTextChange: (String) -> Unit
) {
    Column {

        DialogSearchView(
            searchState = searchState,
            onTextChange = onTextChange
        )
        //val context = LocalContext.current
        /*val countries = remember { countryList(context) }
        val countriesList = countries.toList()*/



        when(contactUiState)
        {
            is Resource.Error,
            is Resource.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)

                ) {
                    CircularProgressIndicator()
                }
            }
            is Resource.Success -> {
                if(contactUiState.data != null)
                {
                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.padding))
                    ) {
                        items(
                            count = contactUiState.data.size,
                            key = { index -> contactUiState.data[index].id }
                        ) { index ->
                            val contact = contactUiState.data[index]
                            SearchContactPickerListItem(
                                item = contact,
                                onClick = { onSelect(contact) }
                            )
                        }

                    }
                }else{
                    NoDataMessage(
                        title = stringResource(id = R.string.no_contact),
                        details = stringResource(id = R.string.no_contact_details),
                        iconSize = 50.dp,
                        painterRes = R.drawable.person_off,
                        titleTextStyle = MyAppTheme.typography.Regular46,
                        detailsTextStyle = MyAppTheme.typography.Regular44
                    )
                }

            }
        }



    }
}

@Composable
private fun SearchContactPickerListItem(
    item: Contact,
    onClick: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxWidth()
) {
    val imageVector = Icons.Default.Person
    val bgColor = MyAppTheme.colors.lightBlue2


    ListItem(
        onClick = onClick,
        leadingIcon = {
            RoundImage(
                imageVector = imageVector,
                //brush = linearGradientsBrush(MyAppTheme.colors.gradientBlue),
                tint = MyAppTheme.colors.black,
                backGround = bgColor,
                contentDescription = "person"
            )
        },
        content = {
            Column {
                Text(
                    text = item.name,
                    style = MyAppTheme.typography.Semibold52_5,
                    color = MyAppTheme.colors.black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        modifier = modifier,
        paddingValues = PaddingValues(
            horizontal = dimensionResource(id = R.dimen.padding)
        ),
        itemBgColor = MyAppTheme.colors.transparent
    )
}