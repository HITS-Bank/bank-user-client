package ru.hitsbank.clientbankapplication.bank_account.data.model

import ru.hitsbank.clientbankapplication.core.data.model.CurrencyCode

data class BankAccountResponse(
    val accountId: String,
    val accountNumber: String,
    val balance: String,
    val currencyCode: CurrencyCode,
    val blocked: Boolean,
    val closed: Boolean,
)
