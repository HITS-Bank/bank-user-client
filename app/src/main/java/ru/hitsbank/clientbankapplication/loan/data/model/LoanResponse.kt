package ru.hitsbank.clientbankapplication.loan.data.model

data class LoanResponse(
    val number: String,
    val tariff: LoanTariffResponse,
    val amount: String,
    val termInMonths: Int,
    val bankAccountNumber: String,
    val paymentAmount: String,
    val paymentSum: String,
    val nexPaymentDateTime: String,
    val currentDebt: String,
)
