package ru.hitsbank.clientbankapplication.bank_account.domain.model

data class TransferConfirmation(
    val senderAccountId: String,
    val receiverAccountId: String,
    val transferAmount: String,
)
