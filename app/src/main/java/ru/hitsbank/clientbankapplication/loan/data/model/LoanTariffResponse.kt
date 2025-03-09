package ru.hitsbank.clientbankapplication.loan.data.model

data class LoanTariffResponse(
    val id: String,
    val name: String,
    val interestRate: String,
    val createdAt: String,
)
