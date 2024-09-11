package com.indie.apps.pennypal.data.module

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.indie.apps.contacts.data.model.ContactNumInfo

class ContactNumberAndName(
    val id: String,
    val name: String,
    val phoneNumbers: List<String>,
    val currentNumberIndex: MutableState<Int> = mutableIntStateOf(0),
    val expanded: MutableState<Boolean> = mutableStateOf(false)
)

data class ContactNumberAndCode(
    val id: String = "",
    val name: String = "",
    val phoneNumber: String,
    val dialCode: String?
)

fun ContactNumInfo.toContactNumberAndName() = ContactNumberAndName(id, name, phoneNumbers)
fun ContactNumberAndName.toContactNumberAndCode(contactNumber: String, countryCode: String?) = ContactNumberAndCode(id, name, contactNumber, countryCode)
