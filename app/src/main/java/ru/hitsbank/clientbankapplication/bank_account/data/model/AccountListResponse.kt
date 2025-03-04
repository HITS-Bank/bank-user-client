package ru.hitsbank.clientbankapplication.bank_account.data.model

import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountEntity
import ru.hitsbank.clientbankapplication.core.domain.model.PageInfo

data class AccountListResponse(
    val bankAccounts: List<BankAccountEntity>,
    val pageInfo: PageInfo,
)