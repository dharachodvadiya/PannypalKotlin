package com.indie.apps.pennypal.repository

interface BackupRepository {
    suspend fun backup()
    suspend fun restore()
    suspend fun isBackupAvailable(): Boolean
}

