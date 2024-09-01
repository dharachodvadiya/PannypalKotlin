package com.indie.apps.contacts.data.model

data class Contact(
    val id: String,
    val name: String,
    val hasPhoneNumber: Boolean = false
)