package ru.hitsbank.clientbankapplication.loan.domain.model

import ru.hitsbank.bank_common.domain.entity.CurrencyCode

data class LoanEntity(
    val id: String,
    val number: String,
    val tariff: LoanTariffEntity,
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