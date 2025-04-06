package ru.hitsbank.clientbankapplication.bank_account.presentation.event

sealed interface AccountListEffect {

    data object OnCreateAccountError : AccountListEffect

    data object OnFailedToLoadHiddenAccounts : AccountListEffect

    data object OnHideAccountError : AccountListEffect

    data object OnUnhideAccountError : AccountListEffect
}