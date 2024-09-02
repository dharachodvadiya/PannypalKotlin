package com.indie.apps.pennypal.presentation.ui.dialog.contact_picker

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.ContactNumberAndName
import com.indie.apps.pennypal.presentation.ui.component.DialogSearchView
import com.indie.apps.pennypal.presentation.ui.component.NoDataMessage
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.ListItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.Resource

@Composable
fun ContactPickerDialogField(
    onSelect: (ContactNumberAndName) -> Unit,
    searchState: TextFieldState,
    contactUiState: Resource<List<ContactNumberAndName>>,
    onTextChange: (String) -> Unit
) {
    Column {
        DialogSearchView(
            searchState = searchState,
            onTextChange = onTextChange
        )
        when (contactUiState) {
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
                if (contactUiState.data != null) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.padding))
                    ) {
                        items(
                            count = contactUiState.data.size,
                            key = { index -> contactUiState.data[index].id }
                        ) { index ->
                            /*var contact by remember {
                                mutableStateOf(contactUiState.data[index])
                            }*/

                            val contact = contactUiState.data[index]
                            SearchContactPickerListItem(
                                item = contact,
                                onClick = { onSelect(contact) },
                                onNumberChange = {
                                    contactUiState.data[index].currentNumberIndex.value = it
                                    onSelect(contactUiState.data[index])
                                },
                                dropdownClick = {
                                    contactUiState.data[index].expanded.value =
                                        !contactUiState.data[index].expanded.value
                                }
                            )
                        }

                    }
                } else {
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
    item: ContactNumberAndName,
    onClick: () -> Unit,
    onNumberChange: (Int) -> Unit,
    dropdownClick: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxWidth()
) {
    val imageVector = Icons.Default.Person
    val bgColor = MyAppTheme.colors.lightBlue2

    Column(
        modifier = modifier.animateContentSize()
    ) {
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

                    Text(
                        text = item.phoneNumbers[item.currentNumberIndex.value],
                        style = MyAppTheme.typography.Medium33,
                        color = MyAppTheme.colors.gray1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            },
            modifier = modifier,
            paddingValues = PaddingValues(
                horizontal = dimensionResource(id = R.dimen.padding)
            ),
            itemBgColor = MyAppTheme.colors.transparent,
            trailingContent = {
                if (item.phoneNumbers.size > 1) {
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        null,
                        Modifier
                            .rotate(if (item.expanded.value) 180f else 0f)
                            .clickable { dropdownClick() }
                    )

                }
            }
        )

        if (item.expanded.value) {
            item.phoneNumbers.forEachIndexed { index, number ->

                contactNumberItem(
                    phoneNumber = number,
                    selected = index == item.currentNumberIndex.value,
                    onSelected = { onNumberChange(index) }
                )

            }
        }
    }
}

@Composable
private fun contactNumberItem(
    phoneNumber: String,
    selected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(MyAppTheme.colors.transparent)
            .padding(PaddingValues(start = 50.dp))
            .clickable(role = Role.Button) { onSelected() }
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelected
        )

        Text(
            text = phoneNumber,
            style = MyAppTheme.typography.Medium40,
            color = MyAppTheme.colors.gray1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

}