package ru.hitsbank.clientbankapplication.bank_account.data.model

data class TransferConfirmationModel(
    val senderAccountId: String,
    val receiverAccountNumber: String,
    val transferAmount: String,
)
