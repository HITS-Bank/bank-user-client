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
import ru.hitsbank.clientbankapplication.core.domain.common.State
import ru.hitsbank.clientbankapplication.core.navigation.RootDestinations
import ru.hitsbank.clientbankapplication.core.navigation.base.NavigationManager
import ru.hitsbank.clientbankapplication.core.navigation.base.back
import ru.hitsbank.clientbankapplication.core.navigation.base.forwardWithCallbackResult
import ru.hitsbank.clientbankapplication.core.presentation.common.BankUiState
import ru.hitsbank.clientbankapplication.core.presentation.common.getIfSuccess
import ru.hitsbank.clientbankapplication.core.presentation.common.updateIfSuccess
import ru.hitsbank.clientbankapplication.loan.domain.interactor.LoanInteractor
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanEntity
import ru.hitsbank.clientbankapplication.loan.presentation.event.LoanDetailsEffect
import ru.hitsbank.clientbankapplication.loan.presentation.event.LoanDetailsEvent
import ru.hitsbank.clientbankapplication.loan.presentation.mapper.LoanDetailsMapper
import ru.hitsbank.clientbankapplication.loan.presentation.model.LoanDetailsDialogState
import ru.hitsbank.clientbankapplication.loan.presentation.model.LoanDetailsState
import ru.hitsbank.clientbankapplication.loan.presentation.model.getAmount

private const val LOAN_ID = "LOAN_ID"
private const val LOAN_ENTITY_JSON = "LOAN_ENTITY_JSON"

@HiltViewModel(assistedFactory = LoanDetailsViewModel.Factory::class)
class LoanDetailsViewModel @AssistedInject constructor(
    @Assisted(LOAN_ID) private val loanId: String?,
    @Assisted(LOAN_ENTITY_JSON) private val loanEntityJson: String?,
    @Assisted private val isUserBlocked: Boolean,
    private val gson: Gson,
    private val loanInteractor: LoanInteractor,
    private val mapper: LoanDetailsMapper,
    private val navigationManager: NavigationManager,
) : ViewModel() {

    private val _state = MutableStateFlow<BankUiState<LoanDetailsState>>(BankUiState.Loading)
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<LoanDetailsEffect>()
    val effects = _effects.asSharedFlow()

    private lateinit var actualLoanId: String

    init {
        loadLoanDetails()
    }

    fun onEvent(event: LoanDetailsEvent) {
        when (event) {
            is LoanDetailsEvent.OpenBankAccount -> {
                navigationManager.forwardWithCallbackResult(
                    RootDestinations.AccountDetails.withArgs(
                        bankAccountEntityJson = null,
                        accountId = event.accountId,
                        isUserBlocked = isUserBlocked,
                    )
                ) {
                    forceReloadLoanDetails(actualLoanId)
                }
            }

            LoanDetailsEvent.MakeLoanPaymentDialogOpen -> {
                _state.updateIfSuccess { state -> state.copy(dialogState = LoanDetailsDialogState.MakePaymentDialog()) }
            }

            LoanDetailsEvent.MakeLoanPaymentDialogClose -> {
                if (_state.getIfSuccess()?.isPerformingAction == true) return
                _state.updateIfSuccess { state -> state.copy(dialogState = LoanDetailsDialogState.None) }
            }

            is LoanDetailsEvent.ChangeLoanPaymentAmount -> {
                _state.updateIfSuccess { state ->
                    state.copy(
                        dialogState = LoanDetailsDialogState.MakePaymentDialog(
                            amount = event.amount
                        )
                    )
                }
            }

            LoanDetailsEvent.ConfirmLoanPayment -> {
                val amount = _state.getIfSuccess()?.dialogState?.getAmount()
                if (amount != null) {
                    viewModelScope.launch {
                        val makeLoanPaymentRequest =
                            loanInteractor.makeLoanPayment(actualLoanId, amount)
                        makeLoanPaymentRequest.collectLatest { state ->
                            when (state) {
                                State.Loading -> _state.updateIfSuccess { it.copy(isPerformingAction = true) }
                                is State.Error -> {
                                    _state.updateIfSuccess {
                                        it.copy(
                                            isPerformingAction = false,
                                            dialogState = LoanDetailsDialogState.None
                                        )
                                    }
                                    _effects.emit(LoanDetailsEffect.ShowLoanPaymentError)
                                }

                                is State.Success -> {
                                    _state.update {
                                        BankUiState.Ready(
                                            LoanDetailsState.default(mapper.map(state.data), isUserBlocked)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            LoanDetailsEvent.Back -> {
                navigationManager.back()
            }
        }
    }

    private fun loadLoanDetails() {
        if (loanEntityJson != null) {
            val loanEntity = gson.fromJson(loanEntityJson, LoanEntity::class.java)
            actualLoanId = loanEntity.id
            _state.update { BankUiState.Ready(LoanDetailsState.default(mapper.map(loanEntity), isUserBlocked)) }
        } else if (loanId != null) {
            actualLoanId = loanId
            forceReloadLoanDetails(loanId)
        } else {
            _state.update { BankUiState.Error() }
        }
    }

    private fun forceReloadLoanDetails(loanId: String) {
        val loanEntityRequest = loanInteractor.getLoanById(loanId)
        viewModelScope.launch {
            loanEntityRequest.collectLatest { state ->
                _state.update {
                    when (state) {
                        is State.Error -> BankUiState.Error()
                        State.Loading -> BankUiState.Loading
                        is State.Success -> BankUiState.Ready(
                            model = LoanDetailsState.default(mapper.map(state.data), isUserBlocked)
                        )
                    }
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(LOAN_ID) loanId: String?,
            @Assisted(LOAN_ENTITY_JSON) loanEntityJson: String?,
            isUserBlocked: Boolean,
        ): LoanDetailsViewModel
    }
}