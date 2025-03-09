package ru.hitsbank.clientbankapplication.bank_account.domain.repository

import ru.hitsbank.clientbankapplication.bank_account.data.model.CloseAccountRequest
import ru.hitsbank.clientbankapplication.bank_account.data.model.TopUpRequest
import ru.hitsbank.clientbankapplication.bank_account.data.model.WithdrawRequest
import ru.hitsbank.clientbankapplication.bank_account.domain.model.AccountListEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.OperationEntity
import ru.hitsbank.clientbankapplication.bank_account.presentation.compose.AccountNumberRequest
import ru.hitsbank.clientbankapplication.core.domain.common.Completable
import ru.hitsbank.clientbankapplication.core.domain.common.Result

interface IBankAccountRepository {

    suspend fun getAccountList(
        pageSize: Int,
        pageNumber: Int,
    ): Result<AccountListEntity>

    suspend fun createAccount(): Result<BankAccountEntity>

    suspend fun topUp(topUpRequest: TopUpRequest): Result<BankAccountEntity>

    suspend fun withdraw(withdrawRequest: WithdrawRequest): Result<BankAccountEntity>

    suspend fun closeAccount(closeAccountRequest: CloseAccountRequest): Result<Completable>

    suspend fun getBankAccountByNumber(accountNumberRequest: AccountNumberRequest): Result<BankAccountEntity>

    suspend fun getOperationHistory(
        accountNumberRequest: AccountNumberRequest,
        pageSize: Int,
        pageNumber: Int,
    ): Result<List<OperationEntity>>
}