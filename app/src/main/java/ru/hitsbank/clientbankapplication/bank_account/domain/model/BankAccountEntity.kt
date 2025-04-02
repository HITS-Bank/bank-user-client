package ru.hitsbank.clientbankapplication.bank_account.domain.model

import ru.hitsbank.bank_common.domain.entity.CurrencyCode

data class BankAccountEntity(
    val id: String,
    val number: String,
    val balance: String,
    val currencyCode: CurrencyCode,
    val status: BankAccountStatusEntity,
)
