package ru.hitsbank.clientbankapplication.bank_account.domain.model

import ru.hitsbank.clientbankapplication.core.domain.model.PageInfo

data class AccountListEntity(
    val bankAccounts: List<BankAccountEntity>,
    val pageInfo: PageInfo,
)
