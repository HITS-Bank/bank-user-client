package ru.hitsbank.clientbankapplication.core.domain.common

sealed interface Result<out T> {

    data class Success<T : Any>(val data: T) : Result<T>

    data class Error(val throwable: Throwable? = null) : Result<Nothing>

}

fun <T, R : Any> Result<T>.map(mapper: (T) -> R): Result<R> = when (this) {
    is Result.Error -> this
    is Result.Success -> Result.Success(mapper(this.data))
}
