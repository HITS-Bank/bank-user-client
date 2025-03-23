package ru.hitsbank.clientbankapplication.bank_account.data.model

import ru.hitsbank.clientbankapplication.core.data.model.CurrencyCode

data class TopUpRequest(
    val currencyCode: CurrencyCode,
    val amount: String,
)