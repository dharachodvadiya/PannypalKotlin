package com.indie.apps.pennypal.data.module

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.indie.apps.contacts.data.model.ContactNumInfo

class ContactNumberAndName(
    val id: String,
    val name: String,
    val phoneNumbers: List<String>,
    val currentNumberIndex: MutableState<Int> = mutableStateOf(0),
    val expanded: MutableState<Boolean> = mutableStateOf(false)
)

fun ContactNumInfo.toContactNumberAndName() = ContactNumberAndName(id, name, phoneNumbers)
