package com.indie.apps.pennypal.repository

import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class InAppFeedbackRepositoryImpl @Inject constructor(
) : InAppFeedbackRepository {
    private val count = MutableStateFlow(0)
    override fun isShowDialog(): Boolean {
        count.value++
        if (count.value == 3) {
            count.value = 0

            println("aaaa true")
            return true
        }
        println("aaaa false")
        return false
    }

    override fun addEventToShowDialog() {
       // count.value++
    }
}

