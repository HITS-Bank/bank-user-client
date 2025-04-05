package ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel

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
import kotlinx.coroutines.launch
import ru.hitsbank.bank_common.domain.State
import ru.hitsbank.bank_common.presentation.navigation.NavigationManager
import ru.hitsbank.bank_common.presentation.navigation.back
import ru.hitsbank.bank_common.presentation.navigation.backWithJsonResult
import ru.hitsbank.clientbankapplication.bank_account.domain.interactor.BankAccountInteractor
import ru.hitsbank.clientbankapplication.bank_account.domain.model.TransferConfirmation
import ru.hitsbank.clientbankapplication.bank_account.domain.model.TransferInfo
import ru.hitsbank.clientbankapplication.bank_account.presentation.event.TransferDetailsEffect
import ru.hitsbank.clientbankapplication.bank_account.presentation.event.TransferDetailsEvent
import ru.hitsbank.clientbankapplication.bank_account.presentation.mapper.TransferDetailsMapper

@HiltViewModel(assistedFactory = TransferDetailsViewModel.Factory::class)
class TransferDetailsViewModel @AssistedInject constructor(
    @Assisted transferInfoJson: String,
    mapper: TransferDetailsMapper,
    private val gson: Gson,
    private val bankAccountInteractor: BankAccountInteractor,
    private val navigationManager: NavigationManager,
) : ViewModel() {

    private val transferInfo = gson.fromJson(transferInfoJson, TransferInfo::class.java)

    private val _state = MutableStateFlow(mapper.map(transferInfo))
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<TransferDetailsEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: TransferDetailsEvent) {
        when (event) {
            is TransferDetailsEvent.OnBackClick -> {
                if (_state.value.isPerformingAction) return
                navigationManager.back()
            }
            is TransferDetailsEvent.Transfer -> {
                if (_state.value.isPerformingAction) return
                _state.value = _state.value.copy(isPerformingAction = true)
                viewModelScope.launch {
                    val transferConfirmation = TransferConfirmation(
                        senderAccountId = transferInfo.senderAccountInfo.accountId,
                        receiverAccountId = transferInfo.receiverAccountInfo.accountId,
                        transferAmount = transferInfo.transferAmountBeforeConversion,
                    )
                    bankAccountInteractor.transfer(transferConfirmation).collect { state ->
                        when (state) {
                            is State.Loading -> {
                                _state.value = _state.value.copy(isPerformingAction = true)
                            }
                            is State.Error -> {
                                _state.value = _state.value.copy(isPerformingAction = false)
                                _effects.emit(TransferDetailsEffect.TransferError)
                            }
                            is State.Success -> {
                                _state.value = _state.value.copy(isPerformingAction = false)
                                navigationManager.backWithJsonResult(gson, state.data)
                            }
                        }
                    }
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(transferInfoJson: String): TransferDetailsViewModel
    }
}