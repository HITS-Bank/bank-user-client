package ru.hitsbank.clientbankapplication.core.presentation.common

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

sealed interface BankUiState<out T> {

    data object Loading : BankUiState<Nothing>

    data class Error(val error: Throwable? = null) : BankUiState<Nothing>

    data class Ready<T>(val model: T) : BankUiState<T>
}

fun <T> StateFlow<BankUiState<T>>.getIfSuccess(): T? {
    return (this.value as? BankUiState.Ready)?.model
}

fun <T> MutableStateFlow<BankUiState<T>>.updateIfSuccess(reducer: (T) -> T) {
    val readyState = this.value as? BankUiState.Ready
    if (readyState != null) {
        this.update { readyState.copy(reducer(readyState.model)) }
    }
}
