package ru.hitsbank.clientbankapplication.core.data.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response
import ru.hitsbank.clientbankapplication.core.domain.common.Completable
import ru.hitsbank.clientbankapplication.core.domain.common.Result
import ru.hitsbank.clientbankapplication.core.domain.common.State

suspend fun <T : Any> apiCall(
    dispatcher: CoroutineDispatcher,
    block: suspend () -> Result<T>,
): Result<T> = withContext(dispatcher) {
    try {
        block.invoke()
    } catch(e: Exception) {
        Result.Error(e)
    }
}

fun <T> Response<T>.toResult(): Result<T> {
    val body = body()
    return if (isSuccessful && body != null) {
        Result.Success(body)
    } else {
        Result.Error()
    }
}

fun <T, R : Any> Response<T>.toResult(onSuccessMapper: (T) -> R): Result<R> {
    val body = body()
    return if (isSuccessful && body != null) {
        Result.Success(onSuccessMapper(body))
    } else {
        Result.Error()
    }
}

fun <T> Response<T>.toCompletableResult(): Result<Completable> {
    val body = body()
    return if (isSuccessful && body != null) {
        Result.Success(Completable)
    } else {
        Result.Error()
    }
}

fun <T> Result<T>.toCompletableResult() = when (this) {
    is Result.Error -> this
    is Result.Success -> Result.Success(Completable)
}

fun <T : Any> Result<T>.toState() = when (this) {
    is Result.Error -> State.Error(throwable)
    is Result.Success -> State.Success(data)
}
