package ru.hitsbank.clientbankapplication.bank_account.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.hitsbank.clientbankapplication.bank_account.data.model.BankAccountResponse
import ru.hitsbank.clientbankapplication.bank_account.data.model.OperationResponse
import ru.hitsbank.clientbankapplication.bank_account.data.model.TopUpRequest
import ru.hitsbank.clientbankapplication.bank_account.data.model.WithdrawRequest
import ru.hitsbank.clientbankapplication.bank_account.presentation.compose.AccountNumberRequest
import ru.hitsbank.clientbankapplication.core.data.model.CurrencyCode

interface BankAccountApi {

    @GET("core/bank_account/list")
    suspend fun getAccountList(
        @Query("pageSize") pageSize: Int,
        @Query("pageNumber") pageNumber: Int,
    ): Response<List<BankAccountResponse>>

    @POST("core/bank_account/create")
    suspend fun createAccount(
        @Query("currencyCode") currencyCode: CurrencyCode,
    ): Response<BankAccountResponse>

    @Deprecated(message = "Use getBankAccountById")
    @POST("core/bank_account/account")
    suspend fun getBankAccountByNumber(
        @Body accountNumberRequest: AccountNumberRequest,
    ): Response<BankAccountResponse>

    @GET("core/bank_account/{accountId}")
    suspend fun getBankAccountById(
        @Path("accountId") accountId: String,
    ): Response<BankAccountResponse>

    @POST("core/bank_account/{accountId}/top_up")
    suspend fun topUp(
        @Path("accountId") accountId: String,
        @Body topUpRequest: TopUpRequest,
    ): Response<BankAccountResponse>

    @POST("core/bank_account/{accountId}/withdraw")
    suspend fun withdraw(
        @Path("accountId") accountId: String,
        @Body withdrawRequest: WithdrawRequest,
    ): Response<BankAccountResponse>

    @POST("core/bank_account/{accountId}/close")
    suspend fun closeAccount(
        @Path("accountId") accountId: String,
    ): Response<Unit>

    // TODO WebSockets
    @POST("core/bank_account/operation_history")
    suspend fun getOperationHistory(
        @Body accountNumberRequest: AccountNumberRequest,
        @Query("pageSize") pageSize: Int,
        @Query("pageNumber") pageNumber: Int,
    ): Response<List<OperationResponse>>
}