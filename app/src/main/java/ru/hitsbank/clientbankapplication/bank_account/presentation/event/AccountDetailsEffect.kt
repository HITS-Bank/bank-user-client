package ru.hitsbank.clientbankapplication.bank_account.presentation.event

sealed interface AccountDetailsEffect {

    data object OnTopUpError : AccountDetailsEffect

    data object OnWithdrawError : AccountDetailsEffect

    data object OnTopUpSuccess : AccountDetailsEffect

    data object OnWithdrawSuccess : AccountDetailsEffect

    data object OnCloseAccountError : AccountDetailsEffect

    data object OnCloseAccountSuccess : AccountDetailsEffect
}