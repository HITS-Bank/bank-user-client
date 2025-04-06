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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.hitsbank.bank_common.Constants.DEFAULT_PAGE_SIZE
import ru.hitsbank.bank_common.domain.State
import ru.hitsbank.bank_common.domain.entity.CurrencyCode
import ru.hitsbank.bank_common.domain.interactor.AuthInteractor
import ru.hitsbank.bank_common.domain.map
import ru.hitsbank.bank_common.presentation.common.BankUiState
import ru.hitsbank.bank_common.presentation.common.getIfSuccess
import ru.hitsbank.bank_common.presentation.common.updateIfSuccess
import ru.hitsbank.bank_common.presentation.navigation.NavigationManager
import ru.hitsbank.bank_common.presentation.navigation.back
import ru.hitsbank.bank_common.presentation.navigation.backWithJsonResult
import ru.hitsbank.bank_common.presentation.navigation.forwardWithCallbackResult
import ru.hitsbank.bank_common.presentation.pagination.PageInfo
import ru.hitsbank.bank_common.presentation.pagination.PaginationEvent
import ru.hitsbank.bank_common.presentation.pagination.PaginationViewModelBase
import ru.hitsbank.clientbankapplication.bank_account.domain.interactor.BankAccountInteractor
import ru.hitsbank.clientbankapplication.bank_account.domain.model.BankAccountShortEntity
import ru.hitsbank.clientbankapplication.bank_account.presentation.event.AccountListEffect
import ru.hitsbank.clientbankapplication.bank_account.presentation.event.AccountListEvent
import ru.hitsbank.clientbankapplication.bank_account.presentation.mapper.AccountListMapper
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountItem
import ru.hitsbank.clientbankapplication.core.navigation.RootDestinations

@HiltViewModel(assistedFactory = AccountListViewModel.Factory::class)
class AccountListViewModel @AssistedInject constructor(
    @Assisted private val accountListMode: AccountListMode,
    private val bankAccountInteractor: BankAccountInteractor,
    private val authInteractor: AuthInteractor,
    private val mapper: AccountListMapper,
    private val navigationManager: NavigationManager,
    private val gson: Gson,
) : PaginationViewModelBase<AccountItem, AccountListPaginationState>(
    BankUiState.Ready(AccountListPaginationState.default(accountListMode))
) {

    private val _effects = Channel<AccountListEffect>()
    val effects = _effects.receiveAsFlow()

    private lateinit var hiddenAccountIds: List<String>

    init {
        onPaginationEvent(PaginationEvent.Reload)
        loadIsUserBlocked()
    }

    override fun getNextPage(pageNumber: Int): Flow<State<PageInfo<AccountItem>>> {
        return bankAccountInteractor.getAccountList(
            pageSize = DEFAULT_PAGE_SIZE,
            pageNumber = pageNumber,
        ).onStart {
            if (!this@AccountListViewModel::hiddenAccountIds.isInitialized || pageNumber == 1) {
                bankAccountInteractor.getHiddenAccountIds().collectLatest { state ->
                    when (state) {
                        State.Loading -> Unit
                        is State.Error -> {
                            hiddenAccountIds = emptyList()
                            sendEffect(AccountListEffect.OnFailedToLoadHiddenAccounts)
                        }
                        is State.Success -> {
                            hiddenAccountIds = state.data
                        }
                    }
                }
            }
        }.map { state ->
            state.map { list ->
                val actualList = list.copy(
                    bankAccounts = list.bankAccounts.filter { account ->
                        when (accountListMode) {
                            AccountListMode.SELECTION -> account.currencyCode == CurrencyCode.RUB
                            else -> true
                        }
                    }
                )
                PageInfo(
                    content = mapper.map(actualList, hiddenAccountIds).filter { account ->
                        when (accountListMode) {
                            AccountListMode.DEFAULT -> !account.isHidden
                            AccountListMode.HIDDEN_ACCOUNTS -> account.isHidden
                            AccountListMode.SELECTION -> true
                        }
                    },
                    paginationFinished = list.bankAccounts.size < DEFAULT_PAGE_SIZE,
                )
            }
        }
    }

    fun onEvent(event: AccountListEvent) {
        when (event) {
            is AccountListEvent.OnPaginationEvent -> onPaginationEvent(event.event)
            is AccountListEvent.OnClickDetails -> {
                if (accountListMode != AccountListMode.SELECTION) {
                    onClickDetails(event.accountId)
                } else {
                    onSelectedAccount(event.accountNumber, event.accountId)
                }
            }
            AccountListEvent.OnOpenCreateAccountDialog -> onOpenCreateAccountDialog()
            AccountListEvent.OnDismissCreateAccountDialog -> onDismissCreateAccountDialog()
            is AccountListEvent.OnCreateAccount -> {
                onCreateAccount(event.currencyCode)
            }
            AccountListEvent.Back -> {
                navigationManager.back()
            }
            is AccountListEvent.OnSelectAccountCurrencyCode -> {
                _state.updateIfSuccess { state ->
                    state.copy(
                        createAccountDialogState = CreateAccountDialogState.Shown(
                            currencyCode = event.currencyCode,
                            isDropdownExpanded = false,
                        )
                    )
                }
            }
            is AccountListEvent.OnSetAccountCreateDropdownExpanded -> {
                val currentDialogState =
                    _state.getIfSuccess()?.createAccountDialogState as? CreateAccountDialogState.Shown
                        ?: return
                _state.updateIfSuccess { state ->
                    state.copy(
                        createAccountDialogState = CreateAccountDialogState.Shown(
                            currencyCode = currentDialogState.currencyCode,
                            isDropdownExpanded = event.isExpanded,
                        )
                    )
                }
            }
            is AccountListEvent.HideAccount -> hideAccount(event.accountId)
            is AccountListEvent.UnhideAccount -> unhideAccount(event.accountId)
            AccountListEvent.OpenHiddenAccounts -> navigationManager.forwardWithCallbackResult(
                destination = RootDestinations.HiddenAccounts.destination,
            ) {
                onPaginationEvent(PaginationEvent.Reload)
            }
        }
    }

    private fun hideAccount(accountId: String) {
        viewModelScope.launch {
            bankAccountInteractor.hideAccount(accountId).collectLatest { state ->
                when (state) {
                    State.Loading -> {
                        _state.updateIfSuccess { it.copy(isPerformingAction = true) }
                    }

                    is State.Error -> {
                        _state.updateIfSuccess { oldState ->
                            oldState.copy(
                                isPerformingAction = false,
                            )
                        }
                        sendEffect(AccountListEffect.OnHideAccountError)
                    }

                    is State.Success -> {
                        hiddenAccountIds = hiddenAccountIds.toMutableList().apply {
                            add(accountId)
                        }
                        _state.updateIfSuccess { oldState ->
                            oldState.copy(
                                isPerformingAction = false,
                                data = oldState.data.filter { account -> account.id != accountId },
                            )
                        }
                    }
                }
            }
        }
    }

    private fun unhideAccount(accountId: String) {
        viewModelScope.launch {
            bankAccountInteractor.unhideAccount(accountId).collectLatest { state ->
                when (state) {
                    State.Loading -> {
                        _state.updateIfSuccess { it.copy(isPerformingAction = true) }
                    }

                    is State.Error -> {
                        _state.updateIfSuccess { oldState ->
                            oldState.copy(
                                isPerformingAction = false,
                            )
                        }
                        sendEffect(AccountListEffect.OnUnhideAccountError)
                    }

                    is State.Success -> {
                        hiddenAccountIds = hiddenAccountIds.toMutableList().apply {
                            remove(accountId)
                        }
                        _state.updateIfSuccess { oldState ->
                            oldState.copy(
                                isPerformingAction = false,
                                data = oldState.data.filter { account -> account.id != accountId },
                            )
                        }
                    }
                }
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

    private fun onOpenCreateAccountDialog() {
        _state.updateIfSuccess { it.copy(createAccountDialogState = CreateAccountDialogState.Shown(CurrencyCode.RUB, false)) }
    }

    private fun onDismissCreateAccountDialog() {
        _state.updateIfSuccess { it.copy(createAccountDialogState = CreateAccountDialogState.Hidden) }
    }

    private fun onCreateAccount(currencyCode: CurrencyCode) = viewModelScope.launch {
        bankAccountInteractor.createAccount(currencyCode = currencyCode)
            .collectLatest { state ->
                when (state) {
                    State.Loading -> {
                        _state.updateIfSuccess { it.copy(isPerformingAction = true) }
                    }

                    is State.Error -> {
                        _state.updateIfSuccess { oldState ->
                            oldState.copy(
                                isPerformingAction = false,
                                createAccountDialogState = CreateAccountDialogState.Hidden,
                            )
                        }
                        sendEffect(AccountListEffect.OnCreateAccountError)
                    }

                    is State.Success -> {
                        _state.updateIfSuccess {
                            it.copy(
                                isPerformingAction = false,
                                createAccountDialogState = CreateAccountDialogState.Hidden,
                            )
                        }
                        navigationManager.forwardWithCallbackResult(
                            destination = RootDestinations.AccountDetails.withArgs(
                                bankAccountEntityJson = gson.toJson(state.data),
                                accountId = null,
                                isUserBlocked = _state.getIfSuccess()?.isUserBlocked ?: false,
                            ),
                            callback = {
                                onPaginationEvent(PaginationEvent.Reload)
                            },
                        )
                    }
                }
            }
    }

    private fun onClickDetails(id: String) = viewModelScope.launch {
        navigationManager.forwardWithCallbackResult(
            destination = RootDestinations.AccountDetails.withArgs(
                bankAccountEntityJson = null,
                accountId = id,
                isUserBlocked = _state.getIfSuccess()?.isUserBlocked ?: false,
            ),
            callback = {
                onPaginationEvent(PaginationEvent.Reload)
            },
        )
    }

    private fun onSelectedAccount(number: String, id: String) {
        navigationManager.backWithJsonResult(gson, BankAccountShortEntity(id, number))
    }

    private fun sendEffect(effect: AccountListEffect) {
        _effects.trySend(effect)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            accountListMode: AccountListMode,
        ): AccountListViewModel
    }
}