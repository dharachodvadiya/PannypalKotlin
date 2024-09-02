package com.indie.apps.contacts.data.model

data class ContactNumInfo(
    val id: String,
    val name: String,
    val phoneNumbers: MutableList<String>
)