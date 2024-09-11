package com.indie.apps.contacts.data.provider.impl

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.ContactsContract.CommonDataKinds.Phone.CONTACT_ID
import android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
import android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI
import android.provider.ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
import android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER
import android.provider.ContactsContract.CommonDataKinds.StructuredName
import android.provider.ContactsContract.CommonDataKinds.Website
import com.indie.apps.contacts.data.map
import com.indie.apps.contacts.data.mapper.asContact
import com.indie.apps.contacts.data.mapper.asContactDetails
import com.indie.apps.contacts.data.model.Contact
import com.indie.apps.contacts.data.model.ContactDetails
import com.indie.apps.contacts.data.model.ContactNumInfo
import com.indie.apps.contacts.data.model.ContactNumInfos
import com.indie.apps.contacts.data.model.Contacts
import com.indie.apps.contacts.data.provider.ContactsProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Collections


class ContactsProviderImpl(
    private val contentResolver: ContentResolver,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ContactsProvider {

    private fun query(
        uri: Uri,
        projection: Array<String>,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        sort: String? = null
    ): Cursor? {
        return contentResolver.query(uri, projection, selection, selectionArgs, sort)
    }

    override suspend fun getContacts(): Contacts = withContext(dispatcher) {
        fetchContacts() ?: Collections.emptyList()
    }

    private fun fetchContacts(): List<Contact>? {
        val cursor = query(
            uri = ContactsContract.Contacts.CONTENT_URI,
            projection = CONTACTS_PROJECTION,
            sort = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            selection = CONTACT_LIST_SELECT,
            selectionArgs = arrayOf(
                "1"
            )
        )
        val result: List<Contact>? = cursor.map { it.asContact() }
        cursor?.close()
        return result
    }

    override suspend fun searchContactsNameWithPhone(searchString: String): ContactNumInfos =
        withContext(dispatcher) {
            fetchContactNumInfoList(searchString)
        }

    private fun fetchContactNumInfoList(searchString: String): ContactNumInfos {
        val cursor = query(
            uri = CONTENT_URI,
            projection = arrayOf(
                CONTACT_ID,
                DISPLAY_NAME,
                NUMBER
            ),
            sort = DISPLAY_NAME,
            selection = if (searchString.isNotEmpty())
                "$DISPLAY_NAME LIKE ?"
            else
                null,
            selectionArgs = if (searchString.isNotEmpty())
                arrayOf("%$searchString%")
            else
                null
        )

        val contacts = mutableMapOf<String, ContactNumInfo>()
        while (cursor?.moveToNext() == true) {
            val contactId =
                cursor.getString(cursor.getColumnIndexOrThrow(CONTACT_ID))
            val name =
                cursor.getString(cursor.getColumnIndexOrThrow(DISPLAY_NAME))
            val phoneNumber =
                cursor.getString(cursor.getColumnIndexOrThrow(NUMBER))

            // Use contactId as a key to ensure unique contacts
            val contact = contacts[contactId] ?: ContactNumInfo(
                id = contactId,
                name = name,
                phoneNumbers = mutableListOf()
            )
            if (!contact.phoneNumbers.contains(phoneNumber))
                contact.phoneNumbers.add(phoneNumber.replace(" ", "").replace("-", ""))
            contacts[contactId] = contact
        }
        cursor?.close()
        return contacts.values.toList()
    }

    override suspend fun getContactDetails(contactId: String): List<ContactDetails> =
        withContext(dispatcher) {
            fetchContactData(contactId) ?: Collections.emptyList()
        }

    private fun fetchContactData(contactId: String): List<ContactDetails>? {
        val cursor: Cursor? = query(
            uri = ContactsContract.Data.CONTENT_URI,
            projection = CONTACT_DATA_PROJECTION,
            //selection: contact_id = ? AND mimetype IN (?,?,?,?)
            selection = CONTACT_DETAILS_SELECT,
            selectionArgs = arrayOf(
                contactId,
                StructuredName.CONTENT_ITEM_TYPE,
                CONTENT_ITEM_TYPE,
                Email.CONTENT_ITEM_TYPE,
                Website.CONTENT_ITEM_TYPE
            )
        )
        val result: List<ContactDetails>? = cursor?.map { it.asContactDetails() }
        cursor?.close()
        return result
    }

    companion object {

        @JvmStatic
        private val CONTACTS_PROJECTION = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.HAS_PHONE_NUMBER,
        )

        @JvmStatic
        private val CONTACT_DATA_PROJECTION = arrayOf(
            ContactsContract.Data._ID,
            ContactsContract.Data.DISPLAY_NAME_PRIMARY,
            ContactsContract.Data.CONTACT_ID,
            ContactsContract.Data.ACCOUNT_TYPE_AND_DATA_SET,
            ContactsContract.Data.MIMETYPE,
            ContactsContract.Data.DATA1,
            ContactsContract.Data.DATA2,
            ContactsContract.Data.DATA3,
        )

        private const val CONTACT_DETAILS_SELECT = "${ContactsContract.Data.CONTACT_ID}=?" +
                " AND ${ContactsContract.Data.MIMETYPE} IN (?,?,?,?)"

        private const val CONTACT_LIST_SELECT = "${ContactsContract.Contacts.HAS_PHONE_NUMBER}=?"
    }

}

