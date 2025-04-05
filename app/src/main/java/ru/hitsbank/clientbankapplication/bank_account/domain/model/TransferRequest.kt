package ru.hitsbank.clientbankapplication.bank_account.domain.model

data class TransferRequest(
    val senderAccountId: String,
    val receiverAccountNumber: String,
    val transferAmount: String,
)
