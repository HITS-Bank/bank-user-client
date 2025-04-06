package ru.hitsbank.clientbankapplication.bank_account.data.model

import ru.hitsbank.bank_common.domain.entity.CurrencyCode

data class TransferAccountInfoModel(
    val accountId: String,
    val accountNumber: String,
    val accountCurrencyCode: CurrencyCode,
)
