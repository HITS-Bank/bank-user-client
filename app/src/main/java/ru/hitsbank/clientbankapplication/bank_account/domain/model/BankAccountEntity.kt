package ru.hitsbank.clientbankapplication.bank_account.domain.model

data class BankAccountEntity(
    val number: String,
    val balance: String,
    val status: BankAccountStatusEntity,
)
