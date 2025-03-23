package ru.hitsbank.clientbankapplication.bank_account.domain.repository

import ru.hitsbank.clientbankapplication.bank_account.data.model.TopUpRequest
import ru.hitsbank.clientbankapplication.bank_account.data.model.WithdrawRequest
import ru.hitsbank.clientbankapplication.bank_account.domain.model.AccountListEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.OperationEntity
import ru.hitsbank.clientbankapplication.bank_account.presentation.compose.AccountNumberRequest
import ru.hitsbank.clientbankapplication.core.data.model.CurrencyCode
import ru.hitsbank.clientbankapplication.core.domain.common.Completable
import ru.hitsbank.clientbankapplication.core.domain.common.Result

interface IBankAccountRepository {

    suspend fun getAccountList(
        pageSize: Int,
        pageNumber: Int,
    ): Result<AccountListEntity>

    suspend fun createAccount(
        currencyCode: CurrencyCode,
    ): Result<BankAccountEntity>

    suspend fun topUp(
        accountId: String,
        topUpRequest: TopUpRequest,
    ): Result<BankAccountEntity>

    suspend fun withdraw(
        accountId: String,
        withdrawRequest: WithdrawRequest,
    ): Result<BankAccountEntity>

    suspend fun closeAccount(accountId: String): Result<Completable>

    suspend fun getBankAccountById(accountId: String): Result<BankAccountEntity>

    suspend fun getOperationHistory(
        accountNumberRequest: AccountNumberRequest,
        pageSize: Int,
        pageNumber: Int,
    ): Result<List<OperationEntity>>
}