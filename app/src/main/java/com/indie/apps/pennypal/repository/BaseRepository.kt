package com.indie.apps.pennypal.repository

interface BaseRepository<T> {

    suspend fun insert(obj: T) : Long

    suspend fun update(obj: T) : Int
}