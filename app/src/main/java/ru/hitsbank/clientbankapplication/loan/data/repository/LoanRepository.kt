package ru.hitsbank.clientbankapplication.loan.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import ru.hitsbank.clientbankapplication.core.data.common.apiCall
import ru.hitsbank.clientbankapplication.core.data.common.toCompletableResult
import ru.hitsbank.clientbankapplication.core.data.common.toResult
import ru.hitsbank.clientbankapplication.core.domain.common.Result
import ru.hitsbank.clientbankapplication.core.domain.model.PageInfo
import ru.hitsbank.clientbankapplication.loan.data.api.LoanApi
import ru.hitsbank.clientbankapplication.loan.data.mapper.LoanMapper
import ru.hitsbank.clientbankapplication.loan.data.model.LoanPaymentRequest
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanCreateEntity
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanEntity
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanTariffEntity
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanTariffSortingOrder
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanTariffSortingProperty
import ru.hitsbank.clientbankapplication.loan.domain.repository.ILoanRepository
import java.time.LocalDateTime
import java.util.UUID

class LoanRepository(
    private val loanApi: LoanApi,
    private val mapper: LoanMapper
) : ILoanRepository {

    override suspend fun getLoanTariffs(
        pageInfo: PageInfo,
        sortingProperty: LoanTariffSortingProperty,
        sortingOrder: LoanTariffSortingOrder,
        query: String?
    ): Result<List<LoanTariffEntity>> {
        return apiCall(Dispatchers.IO) {
            loanApi.getLoanTariffs(
                sortingProperty = sortingProperty.name,
                sortingOrder = sortingOrder.name,
                pageNumber = pageInfo.pageNumber,
                pageSize = pageInfo.pageSize,
                nameQuery = query,
            ).toResult { page ->
                page.loanTariffs.map { tariff ->
                    mapper.map(tariff)
                }
            }
        }
    }

    override suspend fun getLoanByNumber(number: String): Result<LoanEntity> {
        //TODO
        delay(1000)
        return Result.Success(
            data = LoanEntity(
                number = UUID.randomUUID().toString(),
                tariff = LoanTariffEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Тариф 1",
                    interestRate = "10.0",
                ),
                amount = "1000000",
                termInMonths = 12,
                bankAccountNumber = "12345678901234567890",
                paymentAmount = "100000",
                paymentSum = "1200000",
                nextPaymentDateTime = LocalDateTime.now(),
                currentDebt = "900000",
            )
        )
    }

    override suspend fun getLoans(pageInfo: PageInfo): Result<List<LoanEntity>> {
        return apiCall(Dispatchers.IO) {
            loanApi.getLoansPage(pageSize = pageInfo.pageSize, pageNumber = pageInfo.pageNumber)
                .toResult { page ->
                    page.loans.map { loan ->
                        mapper.map(loan)
                    }
                }
        }
    }

    override suspend fun createLoan(loan: LoanCreateEntity): Result<LoanEntity> {
        return apiCall(Dispatchers.IO) {
            loanApi.createLoan(mapper.map(loan))
                .toResult { loan ->
                    mapper.map(loan)
                }
        }
    }

    override suspend fun makeLoanPayment(loanNumber: String, amount: String): Result<LoanEntity> {
        val paymentResult = apiCall(Dispatchers.IO) {
            loanApi.makeLoanPayment(
                LoanPaymentRequest(
                    loanNumber = loanNumber,
                    paymentAmount = amount
                )
            ).toCompletableResult()
        }
        return if (paymentResult is Result.Success) {
            getLoanByNumber(loanNumber)
        } else {
            Result.Error()
        }
    }
}