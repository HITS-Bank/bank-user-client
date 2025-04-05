package ru.hitsbank.clientbankapplication.bank_account.data.model

data class TransferInfoModel(
    val senderAccountInfo: TransferAccountInfoModel,
    val receiverAccountInfo: TransferAccountInfoModel,
    val transferAmountBeforeConversion: String,
    val transferAmountAfterConversion: String,
)
