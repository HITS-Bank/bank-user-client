package ru.hitsbank.clientbankapplication.core.presentation.pagination

sealed interface PaginationEvent {

    object LoadNextPage : PaginationEvent

    object Reload : PaginationEvent
}