package ru.hitsbank.clientbankapplication.loan.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.hitsbank.bank_common.domain.State
import ru.hitsbank.bank_common.domain.interactor.AuthInteractor
import ru.hitsbank.bank_common.domain.map
import ru.hitsbank.bank_common.presentation.navigation.NavigationManager
import ru.hitsbank.bank_common.presentation.navigation.forwardWithCallbackResult
import ru.hitsbank.clientbankapplication.core.domain.model.PageInfo
import ru.hitsbank.clientbankapplication.core.navigation.RootDestinations
import ru.hitsbank.clientbankapplication.core.presentation.common.BankUiState
import ru.hitsbank.clientbankapplication.core.presentation.common.getIfSuccess
import ru.hitsbank.clientbankapplication.core.presentation.common.updateIfSuccess
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationEvent
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationViewModel
import ru.hitsbank.clientbankapplication.loan.domain.interactor.LoanInteractor
import ru.hitsbank.clientbankapplication.loan.presentation.event.LoanListEvent
import ru.hitsbank.clientbankapplication.loan.presentation.mapper.LoanListMapper
import ru.hitsbank.clientbankapplication.loan.presentation.model.LoanItem
import ru.hitsbank.clientbankapplication.loan.presentation.model.LoanListPaginationState
import javax.inject.Inject

@HiltViewModel
class LoanListViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val loanInteractor: LoanInteractor,
    private val mapper: LoanListMapper,
    private val navigationManager: NavigationManager,
) : PaginationViewModel<LoanItem, LoanListPaginationState>(BankUiState.Ready(LoanListPaginationState.EMPTY)) {

    init {
        onPaginationEvent(PaginationEvent.Reload)
        loadIsUserBlocked()
    }

    fun onEvent(event: LoanListEvent) {
        when (event) {
            LoanListEvent.CreateLoan -> navigationManager.forwardWithCallbackResult(
                RootDestinations.CreateLoan.destinationWithArgs(
                    state.value.getIfSuccess()?.isUserBlocked ?: true
                )
            ) {
                onPaginationEvent(PaginationEvent.Reload)
            }

            is LoanListEvent.OpenLoanDetails -> navigationManager.forwardWithCallbackResult(
                RootDestinations.LoanDetails.withArgs(
                    loanId = event.loanId,
                    loanEntityJson = null,
                    isUserBlocked = state.value.getIfSuccess()?.isUserBlocked ?: true
                )
            ) {
                onPaginationEvent(PaginationEvent.Reload)
            }
        }
    }

    private fun loadIsUserBlocked() = viewModelScope.launch {
        authInteractor.getIsUserBlocked()
            .collectLatest { state ->
                when (state) {
                    is State.Success -> _state.updateIfSuccess { it.copy(isUserBlocked = state.data) }
                    else -> Unit
                }
            }
    }

    override suspend fun getNextPageContents(pageNumber: Int): Flow<State<List<LoanItem>>> {
        return loanInteractor.getLoans(PageInfo(pageNumber = pageNumber, pageSize = PAGE_SIZE))
            .map { state ->
                state.map { list -> list.map { loan -> mapper.map(loan) } }
            }
    }

    companion object {
        const val PAGE_SIZE = 5
    }
}