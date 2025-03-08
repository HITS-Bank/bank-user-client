package ru.hitsbank.clientbankapplication.loan.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.hitsbank.clientbankapplication.core.data.common.toState
import ru.hitsbank.clientbankapplication.core.domain.common.State
import ru.hitsbank.clientbankapplication.core.domain.model.PageInfo
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanCreateEntity
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanEntity
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanTariffEntity
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanTariffSortingOrder
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanTariffSortingProperty
import ru.hitsbank.clientbankapplication.loan.domain.repository.ILoanRepository

class LoanInteractor(private val loanRepository: ILoanRepository) {

    fun getLoanTariffs(
        pageInfo: PageInfo,
        sortingProperty: LoanTariffSortingProperty,
        sortingOrder: LoanTariffSortingOrder,
        query: String? = null,
    ): Flow<State<List<LoanTariffEntity>>> = flow {
        emit(State.Loading)
        emit(
            loanRepository.getLoanTariffs(pageInfo, sortingProperty, sortingOrder, query).toState()
        )
    }

    fun getLoans(pageInfo: PageInfo): Flow<State<List<LoanEntity>>> = flow {
        emit(State.Loading)
        emit(loanRepository.getLoans(pageInfo).toState())
    }

    fun createLoan(loan: LoanCreateEntity): Flow<State<LoanEntity>> = flow {
        emit(State.Loading)
        emit(loanRepository.createLoan(loan).toState())
    }

    fun makeLoanPayment(loanId: String, amount: String): Flow<State<LoanEntity>> = flow {
        emit(State.Loading)
        emit(loanRepository.makeLoanPayment(loanId, amount).toState())
    }
}