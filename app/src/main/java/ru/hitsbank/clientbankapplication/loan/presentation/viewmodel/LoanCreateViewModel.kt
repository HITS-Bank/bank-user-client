package ru.hitsbank.clientbankapplication.loan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.hitsbank.bank_common.domain.State
import ru.hitsbank.bank_common.presentation.common.BankUiState
import ru.hitsbank.bank_common.presentation.navigation.NavigationManager
import ru.hitsbank.bank_common.presentation.navigation.back
import ru.hitsbank.bank_common.presentation.navigation.forwardWithJsonResult
import ru.hitsbank.bank_common.presentation.navigation.replace
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountShortEntity
import ru.hitsbank.clientbankapplication.core.navigation.RootDestinations
import ru.hitsbank.clientbankapplication.loan.domain.interactor.LoanInteractor
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanCreateEntity
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanTariffEntity
import ru.hitsbank.clientbankapplication.loan.presentation.event.create.LoanCreateEffect
import ru.hitsbank.clientbankapplication.loan.presentation.event.create.LoanCreateEvent
import ru.hitsbank.clientbankapplication.loan.presentation.model.create.LoanCreateState

@HiltViewModel(assistedFactory = LoanCreateViewModel.Factory::class)
class LoanCreateViewModel @AssistedInject constructor(
    @Assisted private val isUserBlocked: Boolean,
    private val navigationManager: NavigationManager,
    private val gson: Gson,
    private val loanInteractor: LoanInteractor,
) : ViewModel() {

    private val _state = MutableStateFlow(LoanCreateState.EMPTY)
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<LoanCreateEffect>()
    val effects = _effects.asSharedFlow()

    init {
        refreshLoanRating()
    }

    fun onEvent(event: LoanCreateEvent) {
        when (event) {
            LoanCreateEvent.Back -> {
                if (state.value.isPerformingAction) return
                navigationManager.back()
            }

            is LoanCreateEvent.ChangeAmount -> {
                if (state.value.isPerformingAction) return
                _state.update { state -> state.copy(amount = event.amount) }
            }

            is LoanCreateEvent.ChangeTerm -> {
                if (state.value.isPerformingAction) return
                _state.update { state -> state.copy(termInMonths = event.term) }
            }

            LoanCreateEvent.SelectTariff -> {
                if (state.value.isPerformingAction) return
                navigationManager.forwardWithJsonResult<LoanTariffEntity>(
                    gson,
                    RootDestinations.TariffSelection.destination
                ) { tariff ->
                    if (tariff != null) {
                        _state.update { state ->
                            state.copy(
                                tariffId = tariff.id,
                                tariffName = tariff.name
                            )
                        }
                    }
                }
            }

            LoanCreateEvent.SelectAccount -> {
                if (state.value.isPerformingAction) return
                navigationManager.forwardWithJsonResult<BankAccountShortEntity>(
                    gson,
                    RootDestinations.AccountSelection.destination
                ) { account ->
                    if (account != null) {
                        _state.update { state ->
                            state.copy(
                                accountNumber = account.number,
                                accountId = account.id,
                            )
                        }
                    }
                }
            }

            LoanCreateEvent.ConfirmCreate -> {
                val amount = state.value.amount
                val termInMonths = state.value.termInMonths.toIntOrNull()
                val tariffId = state.value.tariffId
                val accountNumber = state.value.accountNumber
                val accountId = state.value.accountId
                if (termInMonths == null || tariffId == null || accountNumber == null || accountId == null) {
                    return
                }

                viewModelScope.launch {
                    val createRequest = LoanCreateEntity(
                        tariffId = tariffId,
                        amount = amount,
                        termInMonths = termInMonths,
                        bankAccountNumber = accountNumber,
                        bankAccountId = accountId,
                    )
                    loanInteractor.createLoan(createRequest)
                        .collectLatest { state ->
                            when (state) {
                                is State.Error -> {
                                    _state.update { oldState -> oldState.copy(isPerformingAction = false) }
                                    _effects.emit(LoanCreateEffect.ShowLoanCreationError)
                                }

                                State.Loading -> {
                                    _state.update { oldState -> oldState.copy(isPerformingAction = true) }
                                }

                                is State.Success -> {
                                    _state.update { oldState -> oldState.copy(isPerformingAction = false) }
                                    navigationManager.replace(
                                        RootDestinations.LoanDetails.withArgs(
                                            loanEntityJson = gson.toJson(state.data),
                                            loanId = null,
                                            isUserBlocked = isUserBlocked,
                                        )
                                    )
                                }
                            }
                        }
                }
            }

            LoanCreateEvent.ReloadLoanRating -> {
                if (state.value.isPerformingAction) return
                refreshLoanRating()
            }
        }
    }

    private fun refreshLoanRating() {
        viewModelScope.launch {
            loanInteractor.getLoanRating()
                .collect { state ->
                    when (state) {
                        is State.Error -> {
                            _state.update { oldState ->
                                oldState.copy(loanRatingState = BankUiState.Error(state.throwable))
                            }
                        }

                        State.Loading -> {
                            _state.update { oldState ->
                                oldState.copy(loanRatingState = BankUiState.Loading)
                            }
                        }

                        is State.Success -> {
                            _state.update { oldState ->
                                oldState.copy(loanRatingState = BankUiState.Ready(state.data))
                            }
                        }
                    }
                }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            isUserBlocked: Boolean,
        ): LoanCreateViewModel
    }
}