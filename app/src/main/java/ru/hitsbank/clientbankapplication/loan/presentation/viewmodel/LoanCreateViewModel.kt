package ru.hitsbank.clientbankapplication.loan.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountNumberEntity
import ru.hitsbank.clientbankapplication.core.domain.common.State
import ru.hitsbank.clientbankapplication.core.navigation.RootDestinations
import ru.hitsbank.clientbankapplication.core.navigation.base.NavigationManager
import ru.hitsbank.clientbankapplication.core.navigation.base.back
import ru.hitsbank.clientbankapplication.core.navigation.base.forwardWithJsonResult
import ru.hitsbank.clientbankapplication.core.navigation.base.replace
import ru.hitsbank.clientbankapplication.loan.domain.interactor.LoanInteractor
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanCreateEntity
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanEntity
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanTariffEntity
import ru.hitsbank.clientbankapplication.loan.presentation.event.create.LoanCreateEffect
import ru.hitsbank.clientbankapplication.loan.presentation.event.create.LoanCreateEvent
import ru.hitsbank.clientbankapplication.loan.presentation.model.create.LoanCreateState

class LoanCreateViewModel(
    private val isUserBlocked: Boolean,
    private val navigationManager: NavigationManager,
    private val gson: Gson,
    private val loanInteractor: LoanInteractor,
) : ViewModel() {

    private val _state = MutableStateFlow(LoanCreateState.EMPTY)
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<LoanCreateEffect>()
    val effects = _effects.asSharedFlow()

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
                navigationManager.forwardWithJsonResult<BankAccountNumberEntity>(
                    gson,
                    RootDestinations.AccountSelection.destination
                ) { account ->
                    if (account != null) {
                        // TODO Вероятно, нужно также передавать id
                        _state.update { state -> state.copy(accountNumber = account.number) }
                    }
                }
            }

            LoanCreateEvent.ConfirmCreate -> {
                val amount = state.value.amount
                val termInMonths = state.value.termInMonths.toIntOrNull()
                val tariffId = state.value.tariffId
                val accountNumber = state.value.accountNumber
                if (termInMonths == null || tariffId == null || accountNumber == null) {
                    return
                }

                viewModelScope.launch {
                    val createRequest = LoanCreateEntity(
                        tariffId = tariffId,
                        amount = amount,
                        termInMonths = termInMonths,
                        bankAccountNumber = accountNumber,
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
                                    val json = gson.toJson(state.data)
                                    Log.e("AAA", json)
                                    val deserializedData = gson.fromJson(json, LoanEntity::class.java)
                                    Log.e("AAA", deserializedData.toString())
                                    navigationManager.replace(
                                        RootDestinations.LoanDetails.withArgs(
                                            loanEntityJson = gson.toJson(state.data),
                                            loanNumber = null,
                                            isUserBlocked = isUserBlocked,
                                        )
                                    )
                                }
                            }
                        }
                }
            }
        }
    }
}