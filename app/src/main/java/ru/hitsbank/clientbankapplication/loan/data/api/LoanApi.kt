package ru.hitsbank.clientbankapplication.loan.data.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.hitsbank.clientbankapplication.loan.data.model.LoanCreateRequest
import ru.hitsbank.clientbankapplication.loan.data.model.LoanPage
import ru.hitsbank.clientbankapplication.loan.data.model.LoanPaymentRequest
import ru.hitsbank.clientbankapplication.loan.data.model.LoanResponse
import ru.hitsbank.clientbankapplication.loan.data.model.LoanTariffsPage

interface LoanApi {

    @GET("credit/loan/tariffs")
    suspend fun getLoanTariffs(
        @Query("sortingProperty") sortingProperty: String,
        @Query("sortingOrder") sortingOrder: String,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("nameQuery") nameQuery: String? = null,
    ): Response<LoanTariffsPage>

    @POST("credit/loan/create")
    suspend fun createLoan(@Body loanCreateRequest: LoanCreateRequest): Response<LoanResponse>

    @GET("credit/loan/list")
    suspend fun getLoansPage(@Query("pageSize") pageSize: Int, @Query("pageNumber") pageNumber: Int): Response<LoanPage>

    @POST("credit/loan/pay")
    suspend fun makeLoanPayment(@Body paymentRequest: LoanPaymentRequest): Response<ResponseBody>
}