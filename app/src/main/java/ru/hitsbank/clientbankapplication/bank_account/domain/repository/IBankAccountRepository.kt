package ru.hitsbank.clientbankapplication.bank_account.domain.repository

import ru.hitsbank.clientbankapplication.bank_account.domain.model.AccountListEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountEntity
import ru.hitsbank.clientbankapplication.core.domain.common.Result

interface IBankAccountRepository {

    suspend fun getAccountList(
        pageSize: Int,
        pageNumber: Int,
    ): Result<AccountListEntity>

    suspend fun createAccount(): Result<BankAccountEntity>
}