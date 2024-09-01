package com.indie.apps.contacts.data.repo

import com.indie.apps.contacts.common.AppFlow
import com.indie.apps.contacts.common.Result
import com.indie.apps.contacts.data.model.ContactDetails
import com.indie.apps.contacts.data.model.Contacts

/**
 * Repository for contacts
 */
interface ContactsRepository {

    /**
     * The loaded contacts
     */
    val contacts: AppFlow<Contacts>

    /**
     * Reload the contacts from the content provider
     */
    suspend fun reloadContacts()

    /**
     * Read details of our specific contacts
     */
    suspend fun getContactDetails(contactId: String): Result<List<ContactDetails>>
}