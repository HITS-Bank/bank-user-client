package ru.hitsbank.clientbankapplication.loan.data.repository

import kotlinx.coroutines.Dispatchers
import ru.hitsbank.clientbankapplication.core.data.common.apiCall
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
import javax.inject.Inject

class LoanRepository @Inject constructor(
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
                page.map { tariff ->
                    mapper.map(tariff)
                }
            }
        }
    }

    override suspend fun getLoanById(loanId: String): Result<LoanEntity> {
        return apiCall(Dispatchers.IO) {
            loanApi.getLoanById(loanId).toResult { loan ->
                mapper.map(loan)
            }
        }
    }

    override suspend fun getLoans(pageInfo: PageInfo): Result<List<LoanEntity>> {
        return apiCall(Dispatchers.IO) {
            loanApi.getLoansPage(pageSize = pageInfo.pageSize, pageNumber = pageInfo.pageNumber)
                .toResult { page ->
                    page.map { loan ->
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

    override suspend fun makeLoanPayment(loanId: String, amount: String): Result<LoanEntity> {
        return apiCall(Dispatchers.IO) {
            loanApi.makeLoanPayment(
                loanId = loanId,
                paymentRequest = LoanPaymentRequest(amount = amount),
            ).toResult { loan ->
                mapper.map(loan)
            }
        }
    }
}