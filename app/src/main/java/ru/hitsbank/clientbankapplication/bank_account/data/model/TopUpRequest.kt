package ru.hitsbank.clientbankapplication.bank_account.data.model

import ru.hitsbank.bank_common.domain.entity.CurrencyCode

data class TopUpRequest(
    val requestId: String? = null,
    val currencyCode: CurrencyCode,
    val amount: String,
)