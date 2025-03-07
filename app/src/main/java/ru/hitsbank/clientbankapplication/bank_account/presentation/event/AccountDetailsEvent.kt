package ru.hitsbank.clientbankapplication.bank_account.presentation.event

sealed interface AccountDetailsEvent {

    data object OnBack : AccountDetailsEvent

    data object OnOpenTopUpDialog : AccountDetailsEvent

    data object OnOpenWithdrawDialog : AccountDetailsEvent

    data object OnDismissTopUpDialog : AccountDetailsEvent

    data object OnDismissWithdrawDialog : AccountDetailsEvent

    data class OnTopUpAmountChange(val amount: String) : AccountDetailsEvent

    data class OnWithdrawAmountChange(val amount: String) : AccountDetailsEvent

    data object TopUp : AccountDetailsEvent

    data object Withdraw : AccountDetailsEvent

    data object OnOpenCloseAccountDialog : AccountDetailsEvent

    data object OnDismissCloseAccountDialog : AccountDetailsEvent

    data object OnCloseAccount : AccountDetailsEvent
}