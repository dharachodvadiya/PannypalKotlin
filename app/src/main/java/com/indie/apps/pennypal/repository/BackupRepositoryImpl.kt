package com.indie.apps.pennypal.repository

import android.content.Context
import com.google.api.client.http.FileContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.FileList
import com.indie.apps.cpp.data.repository.CountryRepository
import com.indie.apps.pennypal.data.database.db.AppDatabase
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.nio.file.NoSuchFileException
import java.util.Collections
import javax.inject.Inject


class BackupRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository,
    private val countryRepository: CountryRepository,
    private val appDatabase: AppDatabase
) : BackupRepository {


    private val dbPath = context.getDatabasePath(Util.DB_NAME)

    override suspend fun backup() {

        withContext(Dispatchers.IO) {

            try {
                val drive = authRepository.getGoogleDrive()

                if (drive != null) {

                    val parentFile = getOrCreateBackupFolder(drive)
                    val fileList = getAllBackupFiles(drive, parentFile.id)

                    if (fileList.files.isEmpty()) {
                        createOrUpdateBackupFile(drive, null, Util.DB_NAME, parentFile.id)
                        createOrUpdateBackupFile(drive, null, "${Util.DB_NAME}-shm", parentFile.id)
                        createOrUpdateBackupFile(drive, null, "${Util.DB_NAME}-wal", parentFile.id)


                    } else {
                        fileList.files.onEach { driveFile ->
                            createOrUpdateBackupFile(
                                drive,
                                driveFile.id,
                                driveFile.name,
                                parentFile.name
                            )
                        }
                    }

                }

            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    override suspend fun restore() {
        try {
            val drive = authRepository.getGoogleDrive()
            if (drive != null) {

                val parentFile = getOrCreateBackupFolder(drive)
                val fileList = getAllBackupFiles(drive, parentFile.id)
                if (fileList.files.isNotEmpty()) {
                    fileList.files.forEach { driveFile ->

                        val outputStream: OutputStream = withContext(Dispatchers.IO) {
                            FileOutputStream(dbPath.parent?.plus("/${driveFile.name}") ?: "")
                        }

                        drive.files().get(driveFile.id).executeMediaAndDownloadTo(outputStream)
                    }

                    //db Migration After Restore

                    //appDatabase.migrateDatabaseIfNeeded(context, countryRepository)
                    AppDatabase.resetDatabaseInstance(context, countryRepository)
                    return
                }
            }
            throw (NoSuchFileException("No Backup Available"))
        } catch (e: IOException) {
            throw (e)
        }
    }

    override suspend fun isBackupAvailable(): Boolean {
        try {
            val drive = authRepository.getGoogleDrive()
            if (drive != null) {

                val parentFile = getOrCreateBackupFolder(drive)
                val fileList = getAllBackupFiles(drive, parentFile.id)
                return fileList.files.isNotEmpty()
            }
            return false
        } catch (e: IOException) {
            return false
        }
    }

    private suspend fun getAllBackupFiles(drive: Drive, parentId: String): FileList {
        return withContext(Dispatchers.IO) {
            drive.files().list().setSpaces("drive").setQ("'$parentId' in parents").execute()
        }
    }

    private fun createOrUpdateBackupFile(
        drive: Drive, fileId: String?, fileName: String, parentId: String
    ): String {
        val storageFile = com.google.api.services.drive.model.File().apply {
            name = fileName
            mimeType = "application/octet-stream"
        }

        val filePath = File(dbPath.parent, fileName)
        val mediaContent = FileContent("application/octet-stream", filePath)

        if (fileId == null) {
            storageFile.setParents(Collections.singletonList(parentId))
            return drive.files().create(storageFile, mediaContent).execute().id
        } else {
            return drive.files().update(fileId, storageFile, mediaContent).execute().id
        }
    }

    private fun getOrCreateBackupFolder(drive: Drive): com.google.api.services.drive.model.File {

        val fileList = drive.files().list()
            .setQ("name = 'PennypalBackup' and mimeType = 'application/vnd.google-apps.folder'")
            .execute()

        if (fileList.files.isEmpty()) {
            val storageFolder = com.google.api.services.drive.model.File().apply {
                name = "PennypalBackup"
                mimeType = "application/vnd.google-apps.folder"
            }

            return drive.files().create(storageFolder).execute()
        } else {
            return fileList.files.first()
        }
    }
}


