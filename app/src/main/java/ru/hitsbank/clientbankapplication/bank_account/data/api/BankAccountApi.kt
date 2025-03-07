package ru.hitsbank.clientbankapplication.bank_account.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.hitsbank.clientbankapplication.bank_account.data.model.AccountListResponse
import ru.hitsbank.clientbankapplication.bank_account.data.model.BankAccountResponse
import ru.hitsbank.clientbankapplication.bank_account.data.model.CloseAccountRequest
import ru.hitsbank.clientbankapplication.bank_account.data.model.OperationHistoryResponse
import ru.hitsbank.clientbankapplication.bank_account.data.model.OperationResponse
import ru.hitsbank.clientbankapplication.bank_account.data.model.TopUpRequest
import ru.hitsbank.clientbankapplication.bank_account.data.model.WithdrawRequest
import ru.hitsbank.clientbankapplication.bank_account.presentation.compose.AccountNumberRequest

interface BankAccountApi {

    @GET("bank_account/list")
    suspend fun getAccountList(
        @Query("pageSize") pageSize: Int,
        @Query("pageNumber") pageNumber: Int,
    ): Response<AccountListResponse>

    @POST("bank_account/create")
    suspend fun createAccount(): Response<BankAccountResponse>

    // TODO уточнить путь и сигнатуру
    @POST("bank_account/account")
    suspend fun getBankAccountByNumber(
        @Body accountNumberRequest: AccountNumberRequest,
    ): Response<BankAccountResponse>

    @POST("bank_account/top_up")
    suspend fun topUp(
        @Body topUpRequest: TopUpRequest,
    ): Response<BankAccountResponse>

    @POST("bank_account/withdraw")
    suspend fun withdraw(
        @Body withdrawRequest: WithdrawRequest,
    ): Response<BankAccountResponse>

    @POST("bank_account/close")
    suspend fun closeAccount(
        @Body closeAccountRequest: CloseAccountRequest,
    ): Response<Unit>

    @POST("bank_account/operation_history")
    suspend fun getOperationHistory(
        @Body accountNumberRequest: AccountNumberRequest,
        @Query("pageSize") pageSize: Int,
        @Query("pageNumber") pageNumber: Int,
    ): Response<OperationHistoryResponse>
}