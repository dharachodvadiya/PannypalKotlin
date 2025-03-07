package com.indie.apps.pennypal.repository

interface InAppFeedbackRepository {
    fun isShowDialog(): Boolean
    fun addEventToShowDialog()
}

