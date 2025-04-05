package ru.hitsbank.clientbankapplication.bank_account.data.model

data class TransferRequestModel(
    val senderAccountId: String,
    val receiverAccountNumber: String,
    val transferAmount: String,
)
