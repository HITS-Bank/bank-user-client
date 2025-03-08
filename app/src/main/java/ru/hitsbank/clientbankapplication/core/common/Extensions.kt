package ru.hitsbank.clientbankapplication.core.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T: CharSequence> Flow<T>.dropFirstBlank(): Flow<T> {
    return flow {
        var isFirstElementCollected = false
        collect { value ->
            if (!isFirstElementCollected) {
                if (value.isNotBlank()) {
                    emit(value)
                }
                isFirstElementCollected = true
            } else {
                emit(value)
            }
        }
    }
}
