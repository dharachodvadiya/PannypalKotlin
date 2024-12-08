package com.indie.apps.pennypal.repository

import android.content.Context
import com.google.api.client.http.FileContent
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class BackupRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository
) : BackupRepository {


    private val dbPath = context.getDatabasePath(Util.DB_NAME).path

    override suspend fun backup() {

        withContext(Dispatchers.IO) {
            println(" aaa start backup .... $dbPath")
            val storageFile = com.google.api.services.drive.model.File()
            storageFile.setName(Util.DB_NAME);
            storageFile.mimeType = "application/octet-stream"

            val filePath = java.io.File(dbPath)
            val mediaContent = FileContent("application/octet-stream", filePath);

            try {
                val drive = authRepository.getGoogleDrive()
                if (drive != null) {
                    val file = drive.files().create(storageFile, mediaContent).execute();
                    if (file != null) {
                        println("aaaaa success Filename: = ${file.name} id =  ${file.id}")
                    } else {
                        println("aaaaa file null")
                    }
                } else {
                    println("aaaa drive null")
                }


            } catch (e: Exception) {

                println("aaaa fail ${e.message}")
            }
        }
    }

    override suspend fun restore() {
        TODO("Not yet implemented")
    }
}

