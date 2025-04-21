package ru.hitsbank.clientbankapplication.bank_account.data.model

data class TransferConfirmationModel(
    val requestId: String,
    val senderAccountId: String,
    val receiverAccountNumber: String,
    val transferAmount: String,
)
