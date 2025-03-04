package ru.hitsbank.clientbankapplication.bank_account.data.api

import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountStatusEntity

data class BankAccountResponse(
    val number: String,
    val balance: String,
    val status: BankAccountStatusEntity,
)
