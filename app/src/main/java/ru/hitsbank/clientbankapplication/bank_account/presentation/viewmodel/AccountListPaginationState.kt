package ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel

import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountItem
import ru.hitsbank.clientbankapplication.core.constants.Constants.DEFAULT_PAGE_SIZE
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationState
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationStateHolder

data class AccountListPaginationState(
    override val paginationState: PaginationState,
    override val data: List<AccountItem>,
    override val pageNumber: Int,
    override val pageSize: Int,
    val isUserBlocked: Boolean,
    val isCreateAccountDialogShown: Boolean,
    val isCreateAccountLoading: Boolean,
    val isSelectionMode: Boolean,
) : PaginationStateHolder<AccountItem> {

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
            isCreateAccountDialogShown = false,
            isCreateAccountLoading = false,
            isSelectionMode = isSelectionMode,
        )
    }
}
