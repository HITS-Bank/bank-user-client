package ru.hitsbank.clientbankapplication.core.domain.common

sealed interface Result<out T> {

    data class Success<T : Any>(val data: T) : Result<T>

    data class Error(val throwable: Throwable? = null) : Result<Nothing>

}