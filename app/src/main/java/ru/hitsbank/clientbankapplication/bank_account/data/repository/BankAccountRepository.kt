package ru.hitsbank.clientbankapplication.bank_account.data.repository

import kotlinx.coroutines.Dispatchers
import ru.hitsbank.clientbankapplication.bank_account.data.api.BankAccountApi
import ru.hitsbank.clientbankapplication.bank_account.data.mapper.BankAccountMapper
import ru.hitsbank.clientbankapplication.bank_account.data.model.TopUpRequest
import ru.hitsbank.clientbankapplication.bank_account.data.model.WithdrawRequest
import ru.hitsbank.clientbankapplication.bank_account.domain.model.AccountListEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.OperationEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.repository.IBankAccountRepository
import ru.hitsbank.clientbankapplication.bank_account.presentation.compose.AccountNumberRequest
import ru.hitsbank.clientbankapplication.core.data.common.apiCall
import ru.hitsbank.clientbankapplication.core.data.common.toCompletableResult
import ru.hitsbank.clientbankapplication.core.data.common.toResult
import ru.hitsbank.clientbankapplication.core.data.model.CurrencyCode
import ru.hitsbank.clientbankapplication.core.domain.common.Completable
import ru.hitsbank.clientbankapplication.core.domain.common.Result
import ru.hitsbank.clientbankapplication.core.domain.common.map
import javax.inject.Inject

class BankAccountRepository @Inject constructor(
    private val bankAccountApi: BankAccountApi,
    private val mapper: BankAccountMapper,
) : IBankAccountRepository {

    override suspend fun getAccountList(pageSize: Int, pageNumber: Int): Result<AccountListEntity> {
        return apiCall(Dispatchers.IO) {
            bankAccountApi.getAccountList(pageSize, pageNumber)
                .toResult()
                .map(mapper::map)
        }
    }

    override suspend fun createAccount(currencyCode: CurrencyCode): Result<BankAccountEntity> {
        return apiCall(Dispatchers.IO) {
            bankAccountApi.createAccount(currencyCode)
                .toResult()
                .map(mapper::map)
        }
    }

    override suspend fun topUp(
        accountId: String,
        topUpRequest: TopUpRequest,
    ): Result<BankAccountEntity> {
        return apiCall(Dispatchers.IO) {
            bankAccountApi.topUp(accountId, topUpRequest)
                .toResult()
                .map(mapper::map)
        }
    }

    override suspend fun withdraw(
        accountId: String,
        withdrawRequest: WithdrawRequest,
    ): Result<BankAccountEntity> {
        return apiCall(Dispatchers.IO) {
            bankAccountApi.withdraw(accountId, withdrawRequest)
                .toResult()
                .map(mapper::map)
        }
    }

    override suspend fun closeAccount(accountId: String): Result<Completable> {
        return apiCall(Dispatchers.IO) {
            bankAccountApi.closeAccount(accountId)
                .toCompletableResult()
        }
    }

    override suspend fun getBankAccountById(accountId: String): Result<BankAccountEntity> {
        return apiCall(Dispatchers.IO) {
            bankAccountApi.getBankAccountById(accountId)
                .toResult()
                .map(mapper::map)
        }
    }

    override suspend fun getOperationHistory(
        accountNumberRequest: AccountNumberRequest,
        pageSize: Int,
        pageNumber: Int,
    ): Result<List<OperationEntity>> {
        return apiCall(Dispatchers.IO) {
            bankAccountApi.getOperationHistory(
                accountNumberRequest = accountNumberRequest,
                pageSize = pageSize,
                pageNumber = pageNumber,
            )
                .toResult()
                .map(mapper::map)
        }
    }
}