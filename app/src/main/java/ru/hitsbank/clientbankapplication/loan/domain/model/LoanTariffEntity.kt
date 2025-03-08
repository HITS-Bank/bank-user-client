package ru.hitsbank.clientbankapplication.loan.domain.model

data class LoanTariffEntity(
    val id: String,
    val name: String,
    val interestRate: String,
)