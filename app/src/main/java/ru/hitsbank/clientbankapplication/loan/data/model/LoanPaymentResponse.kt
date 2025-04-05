package ru.hitsbank.clientbankapplication.loan.data.model

import ru.hitsbank.bank_common.domain.entity.CurrencyCode
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanPaymentStatus

data class LoanPaymentResponse(
    val id: String,
    val status: LoanPaymentStatus,
    val dateTime: String,
    val amount: String,
    val currencyCode: CurrencyCode,
)
