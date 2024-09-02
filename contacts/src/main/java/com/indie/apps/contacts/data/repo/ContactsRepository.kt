package com.indie.apps.contacts.data.repo

import com.indie.apps.contacts.common.AppFlow
import com.indie.apps.contacts.common.Result
import com.indie.apps.contacts.data.model.ContactDetails
import com.indie.apps.contacts.data.model.ContactNumInfos
import com.indie.apps.contacts.data.model.Contacts
import kotlinx.coroutines.flow.Flow

/**
 * Repository for contacts
 */
interface ContactsRepository {

    /**
     * The loaded contacts
     */
    val contacts: AppFlow<Contacts>
    val contactNumInfos: AppFlow<ContactNumInfos>

    /**
     * Reload the contacts from the content provider
     */
    suspend fun reloadContacts()
    suspend fun searchContactsNameWithPhone(searchString: String): Flow<Result<ContactNumInfos>>

    /**
     * Read details of our specific contacts
     */
    suspend fun getContactDetails(contactId: String): Result<List<ContactDetails>>
}