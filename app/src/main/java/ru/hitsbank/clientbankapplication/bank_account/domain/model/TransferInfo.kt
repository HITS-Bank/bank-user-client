package ru.hitsbank.clientbankapplication.bank_account.domain.model

data class TransferInfo(
    val senderAccountInfo: TransferAccountInfo,
    val receiverAccountInfo: TransferAccountInfo,
    val transferAmountBeforeConversion: String,
    val transferAmountAfterConversion: String,
)
