package ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.hitsbank.clientbankapplication.bank_account.domain.interactor.BankAccountInteractor
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountEntity
import ru.hitsbank.clientbankapplication.bank_account.presentation.compose.AccountNumberRequest
import ru.hitsbank.clientbankapplication.bank_account.presentation.event.AccountDetailsEffect
import ru.hitsbank.clientbankapplication.bank_account.presentation.event.AccountDetailsEvent
import ru.hitsbank.clientbankapplication.bank_account.presentation.mapper.AccountDetailsMapper
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountDetailsScreenModel
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountDetailsTopUpDialogModel
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountDetailsWithdrawDialogModel
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.OperationHistoryItem
import ru.hitsbank.bank_common.Constants.DEFAULT_PAGE_SIZE
import ru.hitsbank.bank_common.domain.State
import ru.hitsbank.bank_common.domain.entity.CurrencyCode
import ru.hitsbank.bank_common.domain.map
import ru.hitsbank.bank_common.presentation.navigation.NavigationManager
import ru.hitsbank.bank_common.presentation.navigation.back
import ru.hitsbank.clientbankapplication.core.presentation.common.BankUiState
import ru.hitsbank.clientbankapplication.core.presentation.common.getIfSuccess
import ru.hitsbank.clientbankapplication.core.presentation.common.updateIfSuccess
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationEvent
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationViewModel
import kotlin.math.min

private const val BANK_ACCOUNT_ENTITY_JSON = "BANK_ACCOUNT_ENTITY_JSON_ARG"
private const val ACCOUNT_ID = "ACCOUNT_ID"

@HiltViewModel(assistedFactory = AccountDetailsViewModel.Factory::class)
class AccountDetailsViewModel @AssistedInject constructor(
    @Assisted(BANK_ACCOUNT_ENTITY_JSON) private val bankAccountEntityJson: String?,
    @Assisted(ACCOUNT_ID) private val accountId: String?,
    @Assisted private val isUserBlocked: Boolean,
    private val gson: Gson,
    private val accountDetailsMapper: AccountDetailsMapper,
    private val navigationManager: NavigationManager,
    private val bankAccountInteractor: BankAccountInteractor,
) : PaginationViewModel<OperationHistoryItem, AccountDetailsScreenModel>(
    BankUiState.Loading,
) {

    private val _effects = Channel<AccountDetailsEffect>()
    val effects = _effects.receiveAsFlow()

    init {
        when {
            bankAccountEntityJson != null -> {
                getAccountDetailsModel()
                onPaginationEvent(PaginationEvent.Reload)
            }

            accountId != null -> {
                getAccountFromApi(accountId)
                onPaginationEvent(PaginationEvent.Reload)
            }

            else -> {
                _state.update { BankUiState.Error() }
            }
        }
    }

    override suspend fun getNextPageContents(pageNumber: Int): Flow<State<List<OperationHistoryItem>>> {
        val accountNumber = _state.getIfSuccess()?.number ?: return flowOf(State.Error())
        return bankAccountInteractor.getOperationHistory(
            accountNumberRequest = AccountNumberRequest(
                accountNumber = accountNumber,
            ),
            pageSize = DEFAULT_PAGE_SIZE,
            pageNumber = pageNumber,
        ).map { state ->
            state.map(accountDetailsMapper::mapToOperationHistoryItems)
        }
    }

    fun onEvent(event: AccountDetailsEvent) {
        when (event) {
            AccountDetailsEvent.OnBack -> onBack()
            AccountDetailsEvent.OnDismissTopUpDialog -> onDismissTopUpDialog()
            AccountDetailsEvent.OnDismissWithdrawDialog -> onDismissWithdrawDialog()
            AccountDetailsEvent.OnOpenTopUpDialog -> onOpenTopUpDialog()
            AccountDetailsEvent.OnOpenWithdrawDialog -> onOpenWithdrawDialog()
            is AccountDetailsEvent.OnTopUpAmountChange -> onTopUpAmountChange(event.amount)
            is AccountDetailsEvent.OnWithdrawAmountChange -> onWithdrawAmountChange(event.amount)
            AccountDetailsEvent.TopUp -> onTopUp()
            AccountDetailsEvent.Withdraw -> onWithdraw()
            AccountDetailsEvent.OnOpenCloseAccountDialog -> onOpenCloseAccountDialog()
            AccountDetailsEvent.OnDismissCloseAccountDialog -> onDismissCloseAccountDialog()
            AccountDetailsEvent.OnCloseAccount -> onCloseAccount()
            is AccountDetailsEvent.OnPaginationEvent -> onPaginationEvent(event.event)
            is AccountDetailsEvent.OnTopUpCurrencyChange -> onTopUpCurrencyChange(event.currencyCode)
            is AccountDetailsEvent.OnTopUpDropdownExpanded -> onTopUpDropdownExpanded(event.isExpanded)
            is AccountDetailsEvent.OnWithdrawCurrencyChange -> onWithdrawCurrencyChange(event.currencyCode)
            is AccountDetailsEvent.OnWithdrawDropdownExpanded -> onWithdrawDropdownExpanded(event.isExpanded)
        }
    }

    private fun getAccountDetailsModel() {
        val bankAccountEntity = bankAccountEntityJson
            ?.let { json ->
                gson.fromJson(json, BankAccountEntity::class.java)
            }

        if (bankAccountEntity == null) {
            _state.update { BankUiState.Error() }
        } else {
            val screenModel = accountDetailsMapper.mapToAccountDetailsScreenModel(
                isUserBlocked = isUserBlocked,
                bankAccountEntity = bankAccountEntity,
            )
            _state.update { BankUiState.Ready(screenModel) }
        }
    }

    private fun getAccountFromApi(
        accountId: String,
        withLoader: Boolean = true,
    ) = viewModelScope.launch {
        bankAccountInteractor.getBankAccountById(
            accountId = accountId,
        ).collectLatest { state ->
            when (state) {
                State.Loading -> {
                    if (withLoader) {
                        _state.update { BankUiState.Loading }
                    }
                }

                is State.Error -> {
                    _state.update { BankUiState.Error() }
                }

                is State.Success -> {
                    val screenModel = accountDetailsMapper.mapToAccountDetailsScreenModel(
                        isUserBlocked = isUserBlocked,
                        bankAccountEntity = state.data,
                    )
                    _state.update { BankUiState.Ready(screenModel) }
                }
            }
        }
    }

    private fun onBack() {
        navigationManager.back()
    }

    private fun onDismissTopUpDialog() {
        _state.updateIfSuccess { state ->
            state.copy(
                topUpDialog = state.topUpDialog.copy(
                    isShown = false,
                    amount = AccountDetailsTopUpDialogModel.DEFAULT_AMOUNT.toString(),
                )
            )
        }
    }

    private fun onDismissWithdrawDialog() {
        _state.updateIfSuccess { state ->
            state.copy(
                withdrawDialog = state.withdrawDialog.copy(
                    isShown = false,
                    amount = AccountDetailsWithdrawDialogModel.DEFAULT_AMOUNT.toString(),
                )
            )
        }
    }

    private fun onOpenTopUpDialog() {
        _state.updateIfSuccess { state ->
            state.copy(
                topUpDialog = state.topUpDialog.copy(
                    isShown = true,
                    amount = AccountDetailsTopUpDialogModel.DEFAULT_AMOUNT.toString(),
                )
            )
        }
    }

    private fun onOpenWithdrawDialog() {
        _state.updateIfSuccess { state ->
            val maxWithdrawAmount = _state.getIfSuccess()?.balance?.substringBefore(".")?.toIntOrNull()
            val defaultWithdrawAmount = min(AccountDetailsWithdrawDialogModel.DEFAULT_AMOUNT, maxWithdrawAmount ?: 0)
            state.copy(
                withdrawDialog = state.withdrawDialog.copy(
                    isShown = true,
                    amount = defaultWithdrawAmount.toString(),
                    isDataValid = defaultWithdrawAmount > 0,
                )
            )
        }
    }

    private fun onTopUpAmountChange(amount: String) {
        val amountFloat = amount.toFloatOrNull()
        _state.updateIfSuccess { state ->
            state.copy(
                topUpDialog = state.topUpDialog.copy(
                    amount = amount,
                    isDataValid = amountFloat != null,
                )
            )
        }
    }

    private fun onWithdrawAmountChange(amount: String) {
        val amountFloat = amount.toFloatOrNull()
        val maxWithdrawAmount = _state.getIfSuccess()?.balance?.toFloatOrNull()
        _state.updateIfSuccess { state ->
            state.copy(
                withdrawDialog = state.withdrawDialog.copy(
                    amount = amount,
                    isDataValid = amountFloat != null
                            && maxWithdrawAmount != null
                            && amountFloat <= maxWithdrawAmount
                            && amountFloat > 0,
                )
            )
        }
    }

    private fun onTopUpCurrencyChange(currencyCode: CurrencyCode) {
        _state.updateIfSuccess { state ->
            state.copy(
                topUpDialog = state.topUpDialog.copy(
                    currencyCode = currencyCode,
                )
            )
        }
    }

    private fun onWithdrawCurrencyChange(currencyCode: CurrencyCode) {
        _state.updateIfSuccess { state ->
            state.copy(
                withdrawDialog = state.withdrawDialog.copy(
                    currencyCode = currencyCode,
                )
            )
        }
    }

    private fun onTopUpDropdownExpanded(isExpanded: Boolean) {
        _state.updateIfSuccess { state ->
            state.copy(
                topUpDialog = state.topUpDialog.copy(
                    isDropdownExpanded = isExpanded,
                )
            )
        }
    }

    private fun onWithdrawDropdownExpanded(isExpanded: Boolean) {
        _state.updateIfSuccess { state ->
            state.copy(
                withdrawDialog = state.withdrawDialog.copy(
                    isDropdownExpanded = isExpanded,
                )
            )
        }
    }

    private fun onTopUp() = viewModelScope.launch {
        val accountId = _state.getIfSuccess()?.id ?: return@launch
        val amount = _state.getIfSuccess()?.topUpDialog?.amount ?: return@launch
        val currencyCode = _state.getIfSuccess()?.topUpDialog?.currencyCode ?: return@launch
        bankAccountInteractor.topUp(
            accountId = accountId,
            topUpRequest = accountDetailsMapper.mapToTopUpRequest(
                currencyCode = currencyCode,
                amount = amount,
            )
        ).collectLatest { state ->
            when (state) {
                State.Loading -> {
                    _state.updateIfSuccess { uiState ->
                        uiState.copy(isOverlayLoading = true)
                    }
                }

                is State.Error -> {
                    _state.updateIfSuccess { uiState ->
                        uiState.copy(
                            isOverlayLoading = false,
                            topUpDialog = uiState.topUpDialog.copy(
                                isShown = false,
                            ),
                        )
                    }
                    sendEffect(AccountDetailsEffect.OnTopUpError)
                }

                is State.Success -> {
                    _state.updateIfSuccess { uiState ->
                        uiState.copy(
                            isOverlayLoading = false,
                            topUpDialog = uiState.topUpDialog.copy(
                                isShown = false,
                                amount = AccountDetailsTopUpDialogModel.DEFAULT_AMOUNT.toString(),
                            ),
                        )
                    }

                    sendEffect(AccountDetailsEffect.OnTopUpSuccess)

                    _state.updateIfSuccess { uiState ->
                        accountDetailsMapper.getUpdatedAccountDetailsScreen(
                            oldModel = uiState,
                            bankAccountEntity = state.data,
                        )
                    }

                    onPaginationEvent(PaginationEvent.Reload)
                }
            }
        }
    }

    private fun onWithdraw() = viewModelScope.launch {
        val accountId = _state.getIfSuccess()?.id ?: return@launch
        val amount = _state.getIfSuccess()?.withdrawDialog?.amount ?: return@launch
        val currencyCode = _state.getIfSuccess()?.withdrawDialog?.currencyCode ?: return@launch
        bankAccountInteractor.withdraw(
            accountId = accountId,
            withdrawRequest = accountDetailsMapper.mapToWithdrawRequest(
                currencyCode = currencyCode,
                amount = amount,
            )
        ).collectLatest { state ->
            when (state) {
                State.Loading -> {
                    _state.updateIfSuccess { uiState ->
                        uiState.copy(isOverlayLoading = true)
                    }
                }

                is State.Error -> {
                    _state.updateIfSuccess { uiState ->
                        uiState.copy(
                            isOverlayLoading = false,
                            withdrawDialog = uiState.withdrawDialog.copy(
                                isShown = false,
                            ),
                        )
                    }
                    sendEffect(AccountDetailsEffect.OnWithdrawError)
                }

                is State.Success -> {
                    _state.updateIfSuccess { uiState ->
                        uiState.copy(
                            isOverlayLoading = false,
                            withdrawDialog = uiState.withdrawDialog.copy(
                                isShown = false,
                                amount = AccountDetailsWithdrawDialogModel.DEFAULT_AMOUNT.toString(),
                            ),
                        )
                    }

                    sendEffect(AccountDetailsEffect.OnWithdrawSuccess)

                    _state.updateIfSuccess { uiState ->
                        accountDetailsMapper.getUpdatedAccountDetailsScreen(
                            oldModel = uiState,
                            bankAccountEntity = state.data,
                        )
                    }

                    onPaginationEvent(PaginationEvent.Reload)
                }
            }
        }
    }

    private fun onCloseAccount() = viewModelScope.launch {
        val accountId = _state.getIfSuccess()?.id ?: return@launch
        bankAccountInteractor.closeAccount(
            accountId = accountId,
        ).collectLatest { state ->
            when (state) {
                is State.Error -> {
                    _state.updateIfSuccess { uiState ->
                        uiState.copy(
                            closeAccountDialog = uiState.closeAccountDialog.copy(
                                isShown = false,
                            ),
                            isOverlayLoading = false,
                        )
                    }
                    sendEffect(AccountDetailsEffect.OnCloseAccountError)
                }

                State.Loading -> {
                    _state.updateIfSuccess { it.copy(isOverlayLoading = true) }
                }

                is State.Success -> {
                    _state.updateIfSuccess { uiState ->
                        uiState.copy(
                            closeAccountDialog = uiState.closeAccountDialog.copy(
                                isShown = false,
                            ),
                            isOverlayLoading = false,
                        )
                    }
                    sendEffect(AccountDetailsEffect.OnCloseAccountSuccess)
                    navigationManager.back()
                }
            }
        }
    }

    private fun onOpenCloseAccountDialog() {
        _state.updateIfSuccess { state ->
            state.copy(
                closeAccountDialog = state.closeAccountDialog.copy(
                    isShown = true,
                )
            )
        }
    }

    private fun onDismissCloseAccountDialog() {
        _state.updateIfSuccess { state ->
            state.copy(
                closeAccountDialog = state.closeAccountDialog.copy(
                    isShown = false,
                )
            )
        }
    }

    private fun sendEffect(effect: AccountDetailsEffect) {
        _effects.trySend(effect)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(BANK_ACCOUNT_ENTITY_JSON) bankAccountEntityJson: String?,
            @Assisted(ACCOUNT_ID) accountId: String?,
            isUserBlocked: Boolean,
        ): AccountDetailsViewModel
    }
}