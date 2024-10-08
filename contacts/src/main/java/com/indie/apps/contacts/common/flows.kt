package com.indie.apps.contacts.common

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

typealias AppFlow<T> = StateFlow<Result<T>>
typealias MutableAppFlow<T> = MutableStateFlow<Result<T>>