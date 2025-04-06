package ru.hitsbank.clientbankapplication.bank_account.presentation.event

sealed interface TransferDetailsEvent {

    data object Transfer : TransferDetailsEvent

    data object OnBackClick : TransferDetailsEvent
}