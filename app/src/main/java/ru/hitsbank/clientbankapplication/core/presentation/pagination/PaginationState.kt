package ru.hitsbank.clientbankapplication.core.presentation.pagination

// Стейт для загрузки конкретной страницы
sealed interface PaginationState {

    object Idle : PaginationState

    object Loading : PaginationState

    object Error : PaginationState

    object EndReached : PaginationState

}

// Глобальный стейт, связан только с загрузкой первой страницы
sealed interface PaginationReloadState {

    object Idle : PaginationReloadState

    object Reloading : PaginationReloadState

    object Error : PaginationReloadState

}