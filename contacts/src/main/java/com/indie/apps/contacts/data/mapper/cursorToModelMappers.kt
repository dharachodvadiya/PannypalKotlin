package com.indie.apps.contacts.data.mapper

import android.database.Cursor
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.ContactsContract.CommonDataKinds.StructuredName
import android.provider.ContactsContract.CommonDataKinds.Website
import android.provider.ContactsContract.Data
import com.indie.apps.contacts.data.getIntByName
import com.indie.apps.contacts.data.getStringByName
import com.indie.apps.contacts.data.model.Contact
import com.indie.apps.contacts.data.model.ContactDetails
import com.indie.apps.contacts.data.optIntByName
import com.indie.apps.contacts.data.optStringByName

fun Cursor.asContact(): Contact {
    return Contact(
        id = getStringByName(ContactsContract.Contacts._ID),
        name = getStringByName(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY),
        hasPhoneNumber = getIntByName(ContactsContract.Contacts.HAS_PHONE_NUMBER) == 1
    )
}

fun Cursor.asContactDetails(): ContactDetails {
    val id = getStringByName(Data._ID)
    val displayName = getStringByName(Data.DISPLAY_NAME_PRIMARY)
    val contactId = getStringByName(Data.CONTACT_ID)
    val accountType = optStringByName(Data.ACCOUNT_TYPE_AND_DATA_SET)
    val mimeType = getStringByName(Data.MIMETYPE)
    //Timber.d("id: $id , mimeType: $mimeType, accountType: $accountType")
    return when (mimeType) {
        StructuredName.CONTENT_ITEM_TYPE -> ContactDetails.Name(
            id = id,
            contactId = contactId,
            accountType = accountType,
            displayName = displayName
        )

        Phone.CONTENT_ITEM_TYPE -> ContactDetails.Phone(
            id = id,
            contactId = contactId,
            accountType = accountType,
            displayName = displayName,
            phoneType = Phone.getTypeLabelResource(optIntByName(Phone.TYPE)),
            phoneNumber = optStringByName(Phone.NUMBER),
            phoneLabel = optStringByName(Phone.LABEL)
        )

        Email.CONTENT_ITEM_TYPE -> ContactDetails.Email(
            id = id,
            contactId = contactId,
            accountType = accountType,
            displayName = displayName,
            email = optStringByName(Email.ADDRESS)
        )

        Website.CONTENT_ITEM_TYPE -> ContactDetails.Website(
            id = id,
            contactId = contactId,
            accountType = accountType,
            displayName = displayName,
            url = optStringByName(Website.URL)
        )

        else -> throw IllegalArgumentException("Unsupported types")
    }

}