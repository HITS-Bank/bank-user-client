package ru.hitsbank.clientbankapplication.bank_account.data.model

data class TopUpRequest(
    val accountNumber: String,
    val amount: String,
)