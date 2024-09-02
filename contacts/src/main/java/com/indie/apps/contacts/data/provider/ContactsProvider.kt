package com.indie.apps.contacts.data.provider

import com.indie.apps.contacts.data.model.ContactDetails
import com.indie.apps.contacts.data.model.ContactNumInfos
import com.indie.apps.contacts.data.model.Contacts

/**
 * Provider for the contacts
 */
interface ContactsProvider {

    /**
     * Fetch all the contacts
     */
    suspend fun getContacts(): Contacts
    suspend fun searchContactsNameWithPhone(searchString: String): ContactNumInfos

    /**
     * Read the details of a contact
     *
     * @param contactId the contact id
     */
    suspend fun getContactDetails(contactId: String): List<ContactDetails>

}