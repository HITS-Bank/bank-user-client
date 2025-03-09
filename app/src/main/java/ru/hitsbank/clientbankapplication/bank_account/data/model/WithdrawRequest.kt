package ru.hitsbank.clientbankapplication.bank_account.data.model

data class WithdrawRequest(
    val accountNumber: String,
    val amount: String,
)
