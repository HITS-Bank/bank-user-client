package ru.hitsbank.clientbankapplication.loan.domain.model

import ru.hitsbank.bank_common.domain.entity.CurrencyCode

data class LoanPaymentEntity(
    val id: String,
    val status: LoanPaymentStatus,
    val dateTime: String,
    val amount: String,
    val currencyCode: CurrencyCode,
)
