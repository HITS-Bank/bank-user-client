package ru.hitsbank.clientbankapplication.bank_account.presentation.event

import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationEvent

sealed interface AccountListEvent {

    data class OnClickDetails(val accountNumber: String) : AccountListEvent

    data class OnPaginationEvent(val event: PaginationEvent) : AccountListEvent

    data object OnOpenCreateAccountDialog : AccountListEvent

    data object OnDismissCreateAccountDialog : AccountListEvent

    data object OnCreateAccount : AccountListEvent
}