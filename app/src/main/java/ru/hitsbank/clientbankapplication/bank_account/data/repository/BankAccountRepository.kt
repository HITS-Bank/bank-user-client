package ru.hitsbank.clientbankapplication.bank_account.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.hitsbank.bank_common.data.model.RequestIdHolder
import ru.hitsbank.bank_common.data.model.getNewRequestId
import ru.hitsbank.bank_common.data.utils.apiCall
import ru.hitsbank.bank_common.data.utils.toCompletableResult
import ru.hitsbank.bank_common.data.utils.toResult
import ru.hitsbank.bank_common.data.websocket.BankAccountHistoryWebsocketManager
import ru.hitsbank.bank_common.domain.Completable
import ru.hitsbank.bank_common.domain.Result
import ru.hitsbank.bank_common.domain.entity.CurrencyCode
import ru.hitsbank.bank_common.domain.map
import ru.hitsbank.clientbankapplication.bank_account.data.api.BankAccountApi
import ru.hitsbank.clientbankapplication.bank_account.data.mapper.BankAccountMapper
import ru.hitsbank.clientbankapplication.bank_account.data.model.TopUpRequest
import ru.hitsbank.clientbankapplication.bank_account.data.model.WithdrawRequest
import ru.hitsbank.clientbankapplication.bank_account.domain.model.AccountListEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.OperationEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.TransferConfirmation
import ru.hitsbank.clientbankapplication.bank_account.domain.model.TransferInfo
import ru.hitsbank.clientbankapplication.bank_account.domain.model.TransferRequest
import ru.hitsbank.clientbankapplication.bank_account.domain.repository.IBankAccountRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BankAccountRepository @Inject constructor(
    private val bankAccountApi: BankAccountApi,
    private val bankAccountHistoryWebsocketManager: BankAccountHistoryWebsocketManager,
    private val mapper: BankAccountMapper,
) : IBankAccountRepository {

    private var createAccountIdHolder: RequestIdHolder? = null
    private var topUpIdHolder: RequestIdHolder? = null
    private var withdrawIdHolder: RequestIdHolder? = null
    private var closeAccountIdHolder: RequestIdHolder? = null
    private var transferIdHolder: RequestIdHolder? = null
    private var hideAccountIdHolder: RequestIdHolder? = null
    private var unhideAccountIdHolder: RequestIdHolder? = null

    override suspend fun getAccountList(pageSize: Int, pageNumber: Int): Result<AccountListEntity> {
        return apiCall(Dispatchers.IO) {
            bankAccountApi.getAccountList(pageSize, pageNumber)
                .toResult()
                .map(mapper::map)
        }
    }

    override suspend fun createAccount(currencyCode: CurrencyCode): Result<BankAccountEntity> {
        val idHolder = createAccountIdHolder.getNewRequestId(currencyCode.hashCode())
        createAccountIdHolder = idHolder

        return apiCall(Dispatchers.IO) {
            bankAccountApi.createAccount(currencyCode, idHolder.requestId)
                .also { response ->
                    if (response.isSuccessful) {
                        createAccountIdHolder = null
                    }
                }
                .toResult()
                .map(mapper::map)
        }
    }

    override suspend fun topUp(
        accountId: String,
        topUpRequest: TopUpRequest,
    ): Result<BankAccountEntity> {
        val hashCode = accountId.hashCode() * 31 + topUpRequest.hashCode()
        val idHolder = topUpIdHolder.getNewRequestId(hashCode)
        topUpIdHolder = idHolder

        return apiCall(Dispatchers.IO) {
            bankAccountApi.topUp(accountId, topUpRequest.copy(requestId = idHolder.requestId))
                .also { response ->
                    if (response.isSuccessful) {
                        topUpIdHolder = null
                    }
                }
                .toResult()
                .map(mapper::map)
        }
    }

    override suspend fun withdraw(
        accountId: String,
        withdrawRequest: WithdrawRequest,
    ): Result<BankAccountEntity> {
        val hashCode = accountId.hashCode() * 31 + withdrawRequest.hashCode()
        val idHolder = withdrawIdHolder.getNewRequestId(hashCode)
        withdrawIdHolder = idHolder

        return apiCall(Dispatchers.IO) {
            bankAccountApi.withdraw(accountId, withdrawRequest.copy(requestId = idHolder.requestId))
                .also { response ->
                    if (response.isSuccessful) {
                        withdrawIdHolder = null
                    }
                }
                .toResult()
                .map(mapper::map)
        }
    }

    override suspend fun closeAccount(accountId: String): Result<Completable> {
        val idHolder = closeAccountIdHolder.getNewRequestId(accountId.hashCode())
        closeAccountIdHolder = idHolder

        return apiCall(Dispatchers.IO) {
            bankAccountApi.closeAccount(accountId, idHolder.requestId)
                .also { response ->
                    if (response.isSuccessful) {
                        closeAccountIdHolder = null
                    }
                }
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
        accountId: String,
        pageSize: Int,
        pageNumber: Int,
    ): Result<List<OperationEntity>> {
        return apiCall(Dispatchers.IO) {
            bankAccountApi.getOperationHistory(
                accountId = accountId,
                pageSize = pageSize,
                pageNumber = pageNumber,
            )
                .toResult()
                .map(mapper::map)
        }
    }

    override fun getOperationHistoryUpdates(accountId: String): Result<Flow<OperationEntity>> {
        return runCatching {
            bankAccountHistoryWebsocketManager
                .accountHistoryUpdatesFlow(accountId)
                .map(mapper::map)
                .flowOn(Dispatchers.IO)
        }.map { flow ->
            Result.Success(flow)
        }.getOrElse { throwable ->
            Result.Error(throwable)
        }
    }

    override suspend fun getTransferInfo(transferRequest: TransferRequest): Result<TransferInfo> {
        return apiCall(Dispatchers.IO) {
            bankAccountApi.getTransferInfo(mapper.map(transferRequest))
                .toResult()
                .map(mapper::map)
        }
    }

    override suspend fun transfer(confirmation: TransferConfirmation): Result<BankAccountEntity> {
        val idHolder = transferIdHolder.getNewRequestId(confirmation.hashCode())
        transferIdHolder = idHolder

        return apiCall(Dispatchers.IO) {
            bankAccountApi.transfer(mapper.map(idHolder.requestId, confirmation))
                .also { response ->
                    if (response.isSuccessful) {
                        transferIdHolder = null
                    }
                }
                .toResult()
                .map(mapper::map)
        }
    }

    override suspend fun getHiddenAccountIds(): Result<List<String>> {
        return apiCall(Dispatchers.IO) {
            bankAccountApi.getHiddenAccounts()
                .toResult()
                .map { it.accounts }
        }
    }

    override suspend fun hideAccount(accountId: String): Result<Completable> {
        val idHolder = hideAccountIdHolder.getNewRequestId(accountId.hashCode())
        hideAccountIdHolder = idHolder

        return apiCall(Dispatchers.IO) {
            bankAccountApi.hideAccount(accountId, idHolder.requestId)
                .also { response ->
                    if (response.isSuccessful) {
                        hideAccountIdHolder = null
                    }
                }
                .toCompletableResult()
        }
    }

    override suspend fun unhideAccount(accountId: String): Result<Completable> {
        val idHolder = unhideAccountIdHolder.getNewRequestId(accountId.hashCode())
        unhideAccountIdHolder = idHolder

        return apiCall(Dispatchers.IO) {
            bankAccountApi.unhideAccount(accountId, idHolder.requestId)
                .also { response ->
                    if (response.isSuccessful) {
                        unhideAccountIdHolder = null
                    }
                }
                .toCompletableResult()
        }
    }
}