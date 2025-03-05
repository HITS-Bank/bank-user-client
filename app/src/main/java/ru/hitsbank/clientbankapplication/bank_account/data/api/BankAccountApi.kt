package ru.hitsbank.clientbankapplication.bank_account.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.hitsbank.clientbankapplication.bank_account.data.model.AccountListResponse

interface BankAccountApi {

    @GET("bank_account/list")
    suspend fun getAccountList(
        @Query("pageSize") pageSize: Int,
        @Query("pageNumber") pageNumber: Int,
    ): Response<AccountListResponse>
}