package ru.hitsbank.clientbankapplication.bank_account.data.model

data class BankAccountResponse(
    val accountNumber: String,
    val balance: String,
    val blocked: Boolean,
    val closed: Boolean,
)
