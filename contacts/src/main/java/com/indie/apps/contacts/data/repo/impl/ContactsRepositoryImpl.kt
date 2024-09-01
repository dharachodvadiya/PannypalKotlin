package com.indie.apps.contacts.data.repo.impl

import com.indie.apps.contacts.common.AppFlow
import com.indie.apps.contacts.common.MutableAppFlow
import com.indie.apps.contacts.common.Result
import com.indie.apps.contacts.common.getResult
import com.indie.apps.contacts.data.provider.ContactsProvider
import com.indie.apps.contacts.data.repo.ContactsRepository
import com.indie.apps.contacts.data.model.ContactDetails
import com.indie.apps.contacts.data.model.Contacts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class ContactsRepositoryImpl(
    private val contactsProvider: ContactsProvider,
) : ContactsRepository {

    private val _contactsFlow: MutableAppFlow<Contacts> =
        MutableStateFlow(Result.Loading(initial = true))

    override val contacts: AppFlow<Contacts> by lazy { _contactsFlow.asStateFlow() }

    override suspend fun reloadContacts() {
        _contactsFlow.value = Result.Loading()
        val result = getResult {
            contactsProvider.getContacts()
        }

        _contactsFlow.value = result
    }

    override suspend fun searchContacts(searchString: String): Flow<Result<Contacts>> {
        /*reloadContacts()
        return contacts*/
        return flow{
            val result = getResult {
                contactsProvider.searchContacts(searchString)
            }
            emit(result)
        }
    }

    override suspend fun getContactDetails(
        contactId: String
    ): Result<List<ContactDetails>> {
        return getResult {
            contactsProvider.getContactDetails(contactId)
        }
    }
}