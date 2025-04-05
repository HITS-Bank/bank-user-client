package ru.hitsbank.clientbankapplication.bank_account.domain.model

import ru.hitsbank.bank_common.domain.entity.CurrencyCode

data class TransferAccountInfo(
    val accountId: String,
    val accountNumber: String,
    val accountCurrencyCode: CurrencyCode,
)
