package ru.hitsbank.clientbankapplication.loan.domain.repository

import ru.hitsbank.clientbankapplication.core.domain.model.PageInfo
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanCreateEntity
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanEntity
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanTariffEntity
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanTariffSortingOrder
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanTariffSortingProperty
import ru.hitsbank.bank_common.domain.Result
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanPaymentEntity

interface ILoanRepository {

    suspend fun getLoanTariffs(
        pageInfo: PageInfo,
        sortingProperty: LoanTariffSortingProperty,
        sortingOrder: LoanTariffSortingOrder,
        query: String? = null,
    ): Result<List<LoanTariffEntity>>

    suspend fun getLoanById(
        loanId: String,
    ): Result<LoanEntity>

    suspend fun getLoans(
        pageInfo: PageInfo,
    ): Result<List<LoanEntity>>

    suspend fun createLoan(
        loan: LoanCreateEntity,
    ): Result<LoanEntity>

    suspend fun makeLoanPayment(
        loanId: String,
        amount: String,
    ): Result<LoanEntity>

    suspend fun getLoanRating(): Result<Int>

    suspend fun getLoanPayments(
        loanId: String,
    ): Result<List<LoanPaymentEntity>>
}