package ru.hitsbank.clientbankapplication.bank_account.presentation.event

import ru.hitsbank.bank_common.domain.entity.CurrencyCode
import ru.hitsbank.bank_common.presentation.pagination.PaginationEvent

sealed interface AccountListEvent {

    data class OnClickDetails(
        val accountId: String,
        val accountNumber: String,
    ) : AccountListEvent

    data class OnPaginationEvent(val event: PaginationEvent) : AccountListEvent

    data object OnOpenCreateAccountDialog : AccountListEvent

    data object OnDismissCreateAccountDialog : AccountListEvent

    data class OnCreateAccount(val currencyCode: CurrencyCode) : AccountListEvent

    data class OnSelectAccountCurrencyCode(val currencyCode: CurrencyCode) : AccountListEvent

    data class OnSetAccountCreateDropdownExpanded(val isExpanded: Boolean) : AccountListEvent

    data object Back : AccountListEvent

    data class HideAccount(val accountId: String) : AccountListEvent

    data class UnhideAccount(val accountId: String) : AccountListEvent

    data object OpenHiddenAccounts : AccountListEvent
}