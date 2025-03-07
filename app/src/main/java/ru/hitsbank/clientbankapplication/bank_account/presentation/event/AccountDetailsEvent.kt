package ru.hitsbank.clientbankapplication.bank_account.presentation.event

sealed interface AccountDetailsEvent {

    data object OnBack : AccountDetailsEvent
}