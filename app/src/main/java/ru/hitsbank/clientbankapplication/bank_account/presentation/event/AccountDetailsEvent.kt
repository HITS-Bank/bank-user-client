package ru.hitsbank.clientbankapplication.bank_account.presentation.event

import ru.hitsbank.bank_common.domain.entity.CurrencyCode
import ru.hitsbank.bank_common.presentation.pagination.PaginationEvent

sealed interface AccountDetailsEvent {

    data object OnBack : AccountDetailsEvent

    data object OnOpenTopUpDialog : AccountDetailsEvent

    data object OnOpenWithdrawDialog : AccountDetailsEvent

    data object OnDismissTopUpDialog : AccountDetailsEvent

    data object OnDismissWithdrawDialog : AccountDetailsEvent

    data object OnOpenTransferDialog : AccountDetailsEvent

    data object OnDismissTransferDialog : AccountDetailsEvent

    data class OnTransferAmountChange(val amount: String) : AccountDetailsEvent

    data class OnTransferAccountChange(val accountNumber: String) : AccountDetailsEvent

    data object Transfer : AccountDetailsEvent

    data class OnTopUpAmountChange(val amount: String) : AccountDetailsEvent

    data class OnWithdrawAmountChange(val amount: String) : AccountDetailsEvent

    data class OnTopUpCurrencyChange(val currencyCode: CurrencyCode) : AccountDetailsEvent

    data class OnWithdrawCurrencyChange(val currencyCode: CurrencyCode) : AccountDetailsEvent

    data class OnTopUpDropdownExpanded(val isExpanded: Boolean) : AccountDetailsEvent

    data class OnWithdrawDropdownExpanded(val isExpanded: Boolean) : AccountDetailsEvent

    data object TopUp : AccountDetailsEvent

    data object Withdraw : AccountDetailsEvent

    data object OnOpenCloseAccountDialog : AccountDetailsEvent

    data object OnDismissCloseAccountDialog : AccountDetailsEvent

    data object OnCloseAccount : AccountDetailsEvent

    data class OnPaginationEvent(val event: PaginationEvent) : AccountDetailsEvent
}