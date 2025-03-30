package ru.hitsbank.clientbankapplication.loan.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.hitsbank.clientbankapplication.loan.data.model.LoanCreateRequest
import ru.hitsbank.clientbankapplication.loan.data.model.LoanPaymentRequest
import ru.hitsbank.clientbankapplication.loan.data.model.LoanResponse
import ru.hitsbank.clientbankapplication.loan.data.model.LoanTariffResponse

interface LoanApi {

    @GET("credit/loan/tariffs")
    suspend fun getLoanTariffs(
        @Query("sortingProperty") sortingProperty: String,
        @Query("sortingOrder") sortingOrder: String,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("nameQuery") nameQuery: String? = null,
    ): Response<List<LoanTariffResponse>>

    @GET("credit/loan/{loanId}")
    suspend fun getLoanById(@Path("loanId") loanId: String): Response<LoanResponse>

    @POST("credit/loan/create")
    suspend fun createLoan(@Body loanCreateRequest: LoanCreateRequest): Response<LoanResponse>

    @GET("credit/loan/list")
    suspend fun getLoansPage(
        @Query("pageSize") pageSize: Int,
        @Query("pageNumber") pageNumber: Int,
    ): Response<List<LoanResponse>>

    @POST("credit/loan/{loanId}/pay")
    suspend fun makeLoanPayment(
        @Path("loanId") loanId: String,
        @Body paymentRequest: LoanPaymentRequest,
    ): Response<LoanResponse>
}