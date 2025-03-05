package ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.hitsbank.clientbankapplication.bank_account.domain.interactor.BankAccountInteractor
import ru.hitsbank.clientbankapplication.bank_account.presentation.event.AccountListEvent
import ru.hitsbank.clientbankapplication.bank_account.presentation.mapper.AccountListMapper
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountItem
import ru.hitsbank.clientbankapplication.core.constants.Constants.DEFAULT_PAGE_SIZE
import ru.hitsbank.clientbankapplication.core.domain.common.State
import ru.hitsbank.clientbankapplication.core.domain.common.map
import ru.hitsbank.clientbankapplication.core.domain.interactor.AuthInteractor
import ru.hitsbank.clientbankapplication.core.presentation.common.BankUiState
import ru.hitsbank.clientbankapplication.core.presentation.common.updateIfSuccess
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationEvent
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationViewModel

class AccountListViewModel(
    private val bankAccountInteractor: BankAccountInteractor,
    private val authInteractor: AuthInteractor,
    private val mapper: AccountListMapper,
) : PaginationViewModel<AccountItem, AccountListPaginationState>(
    BankUiState.Ready(AccountListPaginationState.EMPTY)
) {

    init {
        onPaginationEvent(PaginationEvent.Reload)
        loadIsUserBlocked()
    }

    fun onEvent(event: AccountListEvent) {
        when (event) {
            is AccountListEvent.OnClickDetails -> Unit // TODO
            is AccountListEvent.OnPaginationEvent -> onPaginationEvent(event.event)
            AccountListEvent.OnCreateBankAccount -> Unit // TODO
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

    override suspend fun getNextPageContents(pageNumber: Int): Flow<State<List<AccountItem>>> {
        return bankAccountInteractor.getAccountList(
            pageSize = DEFAULT_PAGE_SIZE,
            pageNumber = pageNumber,
        ).map { state ->
            state.map(mapper::map)
        }
    }
}