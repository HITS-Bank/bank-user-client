package ru.hitsbank.clientbankapplication.bank_account.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.hitsbank.bank_common.domain.entity.CurrencyCode
import ru.hitsbank.clientbankapplication.bank_account.data.model.BankAccountResponse
import ru.hitsbank.clientbankapplication.bank_account.data.model.HiddenAccountsResponse
import ru.hitsbank.clientbankapplication.bank_account.data.model.OperationResponse
import ru.hitsbank.clientbankapplication.bank_account.data.model.TopUpRequest
import ru.hitsbank.clientbankapplication.bank_account.data.model.TransferConfirmationModel
import ru.hitsbank.clientbankapplication.bank_account.data.model.TransferInfoModel
import ru.hitsbank.clientbankapplication.bank_account.data.model.TransferRequestModel
import ru.hitsbank.clientbankapplication.bank_account.data.model.WithdrawRequest
import ru.hitsbank.clientbankapplication.bank_account.presentation.compose.AccountNumberRequest

interface BankAccountApi {

    @GET("core/bank_account/list")
    suspend fun getAccountList(
        @Query("pageSize") pageSize: Int,
        @Query("pageNumber") pageNumber: Int,
    ): Response<List<BankAccountResponse>>

    @POST("core/bank_account/create")
    suspend fun createAccount(
        @Query("currencyCode") currencyCode: CurrencyCode,
        @Query("requestId") requestId: String,
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
        @Query("requestId") requestId: String,
    ): Response<Unit>

    // TODO WebSockets
    @POST("core/bank_account/{accountId}/operation_history")
    suspend fun getOperationHistory(
        @Path("accountId") accountId: String,
        @Query("pageSize") pageSize: Int,
        @Query("pageNumber") pageNumber: Int,
    ): Response<List<OperationResponse>>

    @POST("core/bank_account/transferInfo")
    suspend fun getTransferInfo(
        @Body transferRequestModel: TransferRequestModel
    ) : Response<TransferInfoModel>

    @POST("core/bank_account/transfer")
    suspend fun transfer(
        @Body transferConfirmationModel: TransferConfirmationModel
    ) : Response<BankAccountResponse>

    @POST("personalization/hiddenAccount")
    suspend fun hideAccount(
        @Query("accountId") accountId: String,
        @Query("requestId") requestId: String,
    ): Response<Unit>

    @DELETE("personalization/hiddenAccount")
    suspend fun unhideAccount(
        @Query("accountId") accountId: String,
        @Query("requestId") requestId: String,
    ): Response<Unit>

    @GET("personalization/hiddenAccount/list")
    suspend fun getHiddenAccounts(): Response<HiddenAccountsResponse>
}