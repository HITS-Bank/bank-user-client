package ru.hitsbank.clientbankapplication.bank_account.presentation.event

sealed interface AccountListEffect {

    data object OnCreateAccountError : AccountListEffect
}