package ru.hitsbank.clientbankapplication.loan.domain.model

data class LoanCreateEntity(
    val tariffId: String,
    val amount: String,
    val termInMonths: Int,
    val bankAccountId: String,
    val bankAccountNumber: String,
)
