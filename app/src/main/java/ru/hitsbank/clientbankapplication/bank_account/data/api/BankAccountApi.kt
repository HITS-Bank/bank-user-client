package ru.hitsbank.clientbankapplication.bank_account.data.api

import retrofit2.Response
import retrofit2.http.GET
import ru.hitsbank.clientbankapplication.bank_account.data.model.AccountListResponse

interface BankAccountApi {

    @GET("bank_account/list")
    suspend fun getAccountList(pageSize: Int, pageNumber: Int): Response<AccountListResponse>
}