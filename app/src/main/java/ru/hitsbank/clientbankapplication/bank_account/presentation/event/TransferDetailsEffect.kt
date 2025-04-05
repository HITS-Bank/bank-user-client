package ru.hitsbank.clientbankapplication.bank_account.presentation.event

sealed interface TransferDetailsEffect {

    data object TransferError : TransferDetailsEffect
}