package ru.hitsbank.clientbankapplication.bank_account.domain.model

import ru.hitsbank.clientbankapplication.core.data.model.CurrencyCode

data class BankAccountEntity(
    val id: String,
    val number: String,
    val balance: String,
    val currencyCode: CurrencyCode,
    val status: BankAccountStatusEntity,
)
