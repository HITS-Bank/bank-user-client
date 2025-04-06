package ru.hitsbank.clientbankapplication.bank_account.domain.repository

import ru.hitsbank.bank_common.domain.Completable
import ru.hitsbank.bank_common.domain.Result
import ru.hitsbank.bank_common.domain.entity.CurrencyCode
import ru.hitsbank.clientbankapplication.bank_account.data.model.TopUpRequest
import ru.hitsbank.clientbankapplication.bank_account.data.model.WithdrawRequest
import ru.hitsbank.clientbankapplication.bank_account.domain.model.AccountListEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.OperationEntity
import ru.hitsbank.clientbankapplication.bank_account.domain.model.TransferConfirmation
import ru.hitsbank.clientbankapplication.bank_account.domain.model.TransferInfo
import ru.hitsbank.clientbankapplication.bank_account.domain.model.TransferRequest

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
        accountId: String,
        pageSize: Int,
        pageNumber: Int,
    ): Result<List<OperationEntity>>

    suspend fun getTransferInfo(
        transferRequest: TransferRequest
    ): Result<TransferInfo>

    suspend fun transfer(
        confirmation: TransferConfirmation
    ): Result<BankAccountEntity>

    suspend fun getHiddenAccountIds(): Result<List<String>>

    suspend fun hideAccount(accountId: String): Result<Completable>

    suspend fun unhideAccount(accountId: String): Result<Completable>
}