package ru.hitsbank.clientbankapplication.loan.data.repository

import kotlinx.coroutines.Dispatchers
import ru.hitsbank.bank_common.data.model.RequestIdHolder
import ru.hitsbank.bank_common.data.model.getNewRequestId
import ru.hitsbank.bank_common.data.utils.apiCall
import ru.hitsbank.bank_common.data.utils.toCompletableResult
import ru.hitsbank.bank_common.data.utils.toResult
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
import ru.hitsbank.bank_common.domain.Result
import ru.hitsbank.bank_common.domain.map
import ru.hitsbank.bank_common.domain.repository.IProfileRepository
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanPaymentEntity
import javax.inject.Inject

class LoanRepository @Inject constructor(
    private val loanApi: LoanApi,
    private val mapper: LoanMapper,
    private val profileRepository: IProfileRepository,
) : ILoanRepository {

    private var createLoanIdHolder: RequestIdHolder? = null
    private var makeLoanPaymentIdHolder: RequestIdHolder? = null

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
        val idHolder = createLoanIdHolder.getNewRequestId(loan.hashCode())
        createLoanIdHolder = idHolder

        return apiCall(Dispatchers.IO) {
            loanApi.createLoan(mapper.map(idHolder.requestId, loan))
                .also { response ->
                    if (response.isSuccessful) {
                        createLoanIdHolder = null
                    }
                }
                .toResult { loan ->
                    mapper.map(loan)
                }
        }
    }

    override suspend fun makeLoanPayment(loanId: String, amount: String): Result<LoanEntity> {
        val hashcode = loanId.hashCode() * 31 + amount.hashCode()
        val idHolder = makeLoanPaymentIdHolder.getNewRequestId(hashcode)
        makeLoanPaymentIdHolder = idHolder

        return apiCall(Dispatchers.IO) {
            val result = loanApi.makeLoanPayment(
                loanId = loanId,
                paymentRequest = LoanPaymentRequest(
                    requestId = idHolder.requestId,
                    amount = amount,
                ),
            ).toCompletableResult()
            when (result) {
                is Result.Error -> return@apiCall result
                is Result.Success -> {
                    makeLoanPaymentIdHolder = null
                    loanApi.getLoanById(loanId).toResult().map { mapper.map(it) }
                }
            }
        }
    }

    override suspend fun getLoanRating(): Result<Int> {
        when (val profile = profileRepository.getSelfProfile()) {
            is Result.Error -> return Result.Error(profile.throwable)
            is Result.Success -> {
                val userId = profile.data.id
                return apiCall(Dispatchers.IO) {
                    loanApi.getLoanUserRating(userId).toResult { rating ->
                        rating.rating
                    }
                }
            }
        }
    }

    override suspend fun getLoanPayments(loanId: String): Result<List<LoanPaymentEntity>> {
        return apiCall(Dispatchers.IO) {
            loanApi.getLoanPayments(loanId).toResult { page ->
                page.map { payment ->
                    mapper.map(payment)
                }
            }
        }
    }
}