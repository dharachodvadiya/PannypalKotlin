package com.indie.apps.contacts.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlin.coroutines.cancellation.CancellationException

sealed interface Result<out T> {
    data class Loading(val initial:Boolean = false): Result<Nothing>
    data class Success<T>(val data: T?) : Result<T>
    data class Error<T>(val exception: Throwable? = null) : Result<T>
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }
        .onStart { emit(Result.Loading()) }
        .catch { emit(Result.Error(it)) }
}

internal inline fun <T> getResult(block: () -> T): Result<T> = try {
    block().let { Result.Success(it) }
} catch (ex: Exception) {
    // propagate cancellation
    if (ex is CancellationException){
        throw ex
    }
    Result.Error(ex)
}
