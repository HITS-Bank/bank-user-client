package ru.hitsbank.clientbankapplication.loan.domain.model

import ru.hitsbank.clientbankapplication.core.data.model.CurrencyCode
import java.time.LocalDateTime

data class LoanEntity(
    val id: String,
    val number: String,
    val tariff: LoanTariffEntity,
    val amount: String,
    val termInMonths: Int,
    val bankAccountId: String,
    val paymentAmount: String,
    val paymentSum: String,
    val currencyCode: CurrencyCode,
    val nextPaymentDateTime: LocalDateTime,
    val currentDebt: String,
)