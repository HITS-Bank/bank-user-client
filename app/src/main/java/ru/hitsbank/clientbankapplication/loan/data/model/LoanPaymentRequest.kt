package ru.hitsbank.clientbankapplication.loan.data.model

data class LoanPaymentRequest(
    val loanNumber: String,
    val paymentAmount: String,
)
