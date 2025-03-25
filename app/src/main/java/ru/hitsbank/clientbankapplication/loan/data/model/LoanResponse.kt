package ru.hitsbank.clientbankapplication.loan.data.model

import ru.hitsbank.clientbankapplication.core.data.model.CurrencyCode

data class LoanResponse(
    val id: String,
    val number: String,
    val tariff: LoanTariffResponse,
    val amount: String,
    val termInMonths: Int,
    val bankAccountId: String,
    val bankAccountNumber: String,
    val paymentAmount: String,
    val paymentSum: String,
    val currencyCode: CurrencyCode,
    val nextPaymentDateTime: String,
    val currentDebt: String,
)
