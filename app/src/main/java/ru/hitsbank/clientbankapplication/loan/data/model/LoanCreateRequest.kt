package ru.hitsbank.clientbankapplication.loan.data.model

data class LoanCreateRequest(
    val tariffId: String,
    val amount: String,
    val termInMonths: Int,
    val bankAccountId: String,
    val bankAccountNumber: String,
)
