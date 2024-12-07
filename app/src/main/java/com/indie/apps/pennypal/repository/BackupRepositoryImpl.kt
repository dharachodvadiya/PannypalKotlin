package com.indie.apps.pennypal.repository

import android.R
import android.content.Context
import com.google.android.gms.common.Scopes
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.http.FileContent
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Collections
import javax.inject.Inject


class BackupRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository : AuthRepository
) : BackupRepository {


    private val dbPath = context.getDatabasePath(Util.DB_NAME).toURI()

    override suspend fun backup() {

        println(" aaa start backup")
        val storageFile = File();
        storageFile.setParents(Collections.singletonList("backup"));
        storageFile.setName(Util.DB_BACKUP_NAME);

        val filePath = java.io.File(dbPath)
        val mediaContent = FileContent("", filePath);

        try {
            val drive = authRepository.getGoogleDrive()
            if(drive != null)
            {
                val file = drive.files().create(storageFile, mediaContent).execute();
                if (file != null) {
                    println("aaaaa success Filename: = ${file.name} id =  ${file.id}")
                }else{
                    println("aaaaa file null")
                }
            }else{
                println("aaaa drive null")
            }


        } catch (e : Exception) {

            println("aaaa fail ${e.message}")
        }
    }

    override suspend fun restore() {
        TODO("Not yet implemented")
    }

}

