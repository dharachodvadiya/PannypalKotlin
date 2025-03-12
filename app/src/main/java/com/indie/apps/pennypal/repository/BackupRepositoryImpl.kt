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
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
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

        try {
            val drive = authRepository.getGoogleDrive()
                ?: throw (IllegalStateException("Google Drive service not available"))

            val parentFile = getOrCreateBackupFolder(drive)
            val fileList = getAllBackupFiles(drive, parentFile.id)

            val dbFiles = listOf(
                Util.DB_NAME,
                "${Util.DB_NAME}-shm",
                "${Util.DB_NAME}-wal"
            )

            /*if (fileList.files.isEmpty()) {
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
            }*/

            if (fileList.files.isEmpty()) {
                // Create new backup files
                dbFiles.forEachIndexed { index, fileName ->
                    val uploadResult = uploadFileWithRetry(drive, null, fileName, parentFile.id)
                    uploadResult.onFailure { e ->
                        throw e
                    }
                    if (index < dbFiles.size - 1) {
                        delay(500) // 500ms delay between uploads
                    }
                }
            } else {
                // Update existing backup files
                fileList.files.forEachIndexed { index, driveFile ->
                    val uploadResult =
                        uploadFileWithRetry(drive, driveFile.id, driveFile.name, parentFile.id)
                    uploadResult.onFailure { e ->
                        throw e
                    }
                    if (index < fileList.files.size - 1) {
                        delay(500) // 500ms delay between uploads
                    }
                }
            }

        } catch (e: Exception) {
            throw (e)
        }
    }

    override suspend fun restore() {
        try {
            val drive = authRepository.getGoogleDrive()
                ?: throw (IllegalStateException("Google Drive service not available"))

            val parentFile = getOrCreateBackupFolder(drive)
            val fileList = getAllBackupFiles(drive, parentFile.id)

            if (fileList.files.isEmpty()) {
                throw (NoSuchFileException("No Backup Available"))
            }

            fileList.files.forEachIndexed { index, driveFile ->
                // Sequential download with retry logic
                val downloadResult = downloadFileWithRetry(drive, driveFile, dbPath, maxRetries = 3)
                downloadResult.onFailure { e ->
                    throw e // Propagate failure to caller
                }

                // Small delay to avoid overwhelming the API (optional)
                if (index < fileList.files.size - 1) {
                    delay(500) // 500ms delay between downloads
                }
            }

            AppDatabase.resetDatabaseInstance(context, countryRepository)

            /*fileList.files.forEach { driveFile ->

                println("aaaa repo 555 111 ${dbPath.parent}")
                println("aaaa repo 555 111 111 ${driveFile.name}")
                withContext(Dispatchers.IO) {

                    println("aaaa repo 555 222")
                    try {
                        val filePath = dbPath.parent?.plus("/${driveFile.name}") ?: throw IllegalStateException("Invalid dbPath parent")
                        FileOutputStream(filePath).use { outputStream ->
                            println("aaaa repo 555 222 111")

                            // Get the file from Drive
                            val file = drive.files().get(driveFile.id)
                            println("aaaa repo 555 222 222 ${file?.toString() ?: "File is null"}")

                            if (file == null) {
                                throw IllegalStateException("Drive file is null")
                            }

                            // Download the file
                            file.executeMediaAndDownloadTo(outputStream)
                            println("aaaa repo 555 222 Download completed")
                        }
                    } catch (e: IOException) {
                        println("aaaa repo 555 222 333 IOException: ${e.message}")
                        e.printStackTrace()
                    } catch (e: Exception) {
                        println("aaaa repo 555 222 333 General Exception: ${e.message}")
                        e.printStackTrace()
                    }
                    println("aaaa repo 555 333")
                }

            }*/
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

    private suspend fun uploadFileWithRetry(
        drive: Drive,
        fileId: String?,
        fileName: String,
        parentId: String,
        maxRetries: Int = 3
    ): Result<Unit> {
        var attempt = 0
        while (attempt < maxRetries) {
            try {
                return withContext(Dispatchers.IO) {
                    val localFile = File(dbPath.parent, fileName)
                    if (!localFile.exists() || localFile.length() == 0L) {
                        throw IllegalStateException("Local file is missing or empty: $fileName")
                    }

                    createOrUpdateBackupFile(drive, fileId, fileName, parentId)
                    Result.success(Unit)
                }
            } catch (e: IOException) {
                attempt++
                if (attempt == maxRetries) {
                    return Result.failure(e)
                }
                delay(1000 * attempt.toLong()) // Exponential backoff: 1s, 2s, 3s
            } catch (e: Exception) {
                return Result.failure(e)
            }
        }
        return Result.failure(IllegalStateException("Unexpected retry loop exit"))
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

    private suspend fun downloadFileWithRetry(
        drive: Drive,
        driveFile: com.google.api.services.drive.model.File,
        dbPath: File,
        maxRetries: Int
    ): Result<Unit> {
        var attempt = 0
        while (attempt < maxRetries) {
            try {
                return withContext(Dispatchers.IO) {
                    val fileMetadata = drive.files().get(driveFile.id)
                        .setFields("id, name, size, mimeType")
                        .execute()

                    if (fileMetadata.size == 0 || fileMetadata.mimeType.contains("folder")) {
                        throw IllegalStateException("File is empty or a folder: ${driveFile.id}")
                    }

                    val filePath = dbPath.parent?.plus("/${driveFile.name}")
                        ?: throw IllegalStateException("Invalid dbPath parent")

                    FileOutputStream(filePath).use { outputStream ->
                        val file = drive.files().get(driveFile.id)
                            ?: throw IllegalStateException("Drive file is null")
                        file.executeMediaAndDownloadTo(outputStream)
                    }
                    Result.success(Unit)
                }
            } catch (e: IOException) {
                attempt++
                if (attempt == maxRetries) {
                    return Result.failure(e)
                }
                delay(1000 * attempt.toLong()) // Exponential backoff: 1s, 2s, 3s
            } catch (e: Exception) {
                return Result.failure(e)
            }
        }
        return Result.failure(IllegalStateException("Unexpected retry loop exit"))
    }
}


