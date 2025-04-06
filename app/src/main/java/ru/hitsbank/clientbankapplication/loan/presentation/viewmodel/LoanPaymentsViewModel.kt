package ru.hitsbank.clientbankapplication.loan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.hitsbank.bank_common.domain.State
import ru.hitsbank.bank_common.presentation.common.BankUiState
import ru.hitsbank.bank_common.presentation.navigation.NavigationManager
import ru.hitsbank.bank_common.presentation.navigation.back
import ru.hitsbank.bank_common.presentation.navigation.forwardWithCallbackResult
import ru.hitsbank.clientbankapplication.core.navigation.RootDestinations
import ru.hitsbank.clientbankapplication.loan.domain.interactor.LoanInteractor
import ru.hitsbank.clientbankapplication.loan.presentation.event.payment.LoanPaymentsEvent
import ru.hitsbank.clientbankapplication.loan.presentation.mapper.LoanPaymentsMapper
import ru.hitsbank.clientbankapplication.loan.presentation.model.LoanPaymentsState

@HiltViewModel(assistedFactory = LoanPaymentsViewModel.Factory::class)
class LoanPaymentsViewModel @AssistedInject constructor(
    @Assisted private val loanId: String,
    @Assisted private val isUserBlocked: Boolean,
    private val navigationManager: NavigationManager,
    private val loanInteractor: LoanInteractor,
    private val mapper: LoanPaymentsMapper,
) : ViewModel() {

    private val _state = MutableStateFlow<BankUiState<LoanPaymentsState>>(BankUiState.Loading)
    val state = _state.asStateFlow()

    init {
        refreshPayments()
    }

    fun onEvent(event: LoanPaymentsEvent) {
        when (event) {
            LoanPaymentsEvent.Back -> navigationManager.back()
            LoanPaymentsEvent.LoanInfoClick -> navigationManager.forwardWithCallbackResult(
                RootDestinations.LoanDetails.withArgs(
                    loanId = loanId,
                    loanEntityJson = null,
                    isUserBlocked = isUserBlocked,
                )
            ) {
                refreshPayments()
            }
            LoanPaymentsEvent.Refresh -> refreshPayments()
        }
    }

    private fun refreshPayments() {
        viewModelScope.launch {
            loanInteractor.getLoanPayments(loanId).collect { paymentsState ->
                when (paymentsState) {
                    State.Loading -> {
                        _state.update { BankUiState.Loading }
                    }
                    is State.Error -> {
                        _state.update { BankUiState.Error(paymentsState.throwable) }
                    }
                    is State.Success -> {
                        val payments = paymentsState.data
                        _state.update {
                            BankUiState.Ready(
                                LoanPaymentsState(payments = payments.map(mapper::map))
                            )
                        }
                    }
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(loanId: String, isUserBlocked: Boolean): LoanPaymentsViewModel
    }
}