package ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel

import ru.hitsbank.bank_common.Constants.DEFAULT_PAGE_SIZE
import ru.hitsbank.bank_common.domain.entity.CurrencyCode
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountItem
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.CurrencyCodeDropdownItem
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationState
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationStateHolder

data class AccountListPaginationState(
    override val paginationState: PaginationState,
    override val data: List<AccountItem>,
    override val pageNumber: Int,
    override val pageSize: Int,
    val isUserBlocked: Boolean,
    val createAccountDialogState: CreateAccountDialogState,
    val isCreateAccountLoading: Boolean,
    val isSelectionMode: Boolean,
) : PaginationStateHolder<AccountItem> {

    val currencyCodeItems = CurrencyCode.entries.map { code -> CurrencyCodeDropdownItem(code) }

    override fun copyWith(
        paginationState: PaginationState,
        data: List<AccountItem>,
        pageNumber: Int
    ): PaginationStateHolder<AccountItem> {
        return copy(paginationState = paginationState, data = data, pageNumber = pageNumber)
    }

    override fun resetPagination(): PaginationStateHolder<AccountItem> {
        return copy(data = emptyList(), pageNumber = 1)
    }

    companion object {
        fun default(isSelectionMode: Boolean) = AccountListPaginationState(
            paginationState = PaginationState.Idle,
            data = emptyList(),
            pageNumber = 1,
            pageSize = DEFAULT_PAGE_SIZE,
            isUserBlocked = false,
            createAccountDialogState = CreateAccountDialogState.Hidden,
            isCreateAccountLoading = false,
            isSelectionMode = isSelectionMode,
        )
    }
}

sealed interface CreateAccountDialogState {

    data object Hidden : CreateAccountDialogState

    data class Shown(
        val currencyCode: CurrencyCode,
        val isDropdownExpanded: Boolean,
    ) : CreateAccountDialogState {

        val currencyCodeItem = CurrencyCodeDropdownItem(currencyCode)
    }
}