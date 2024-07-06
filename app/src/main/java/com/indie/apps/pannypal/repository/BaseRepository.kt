package com.indie.apps.pannypal.repository

interface BaseRepository<T> {

    suspend fun insert(obj: T) : Long

    suspend fun update(obj: T) : Int
}