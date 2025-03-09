package ru.hitsbank.clientbankapplication.loan.presentation.model

import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationState
import ru.hitsbank.clientbankapplication.core.presentation.pagination.PaginationStateHolder
import ru.hitsbank.clientbankapplication.loan.presentation.viewmodel.LoanListViewModel

data class LoanListPaginationState(
    override val paginationState: PaginationState,
    override val data: List<LoanItem>,
    override val pageNumber: Int,
    override val pageSize: Int,
    val isUserBlocked: Boolean,
) : PaginationStateHolder<LoanItem> {

    override fun copyWith(
        paginationState: PaginationState,
        data: List<LoanItem>,
        pageNumber: Int
    ): PaginationStateHolder<LoanItem> {
        return copy(paginationState = paginationState, data = data, pageNumber = pageNumber)
    }

    override fun resetPagination(): PaginationStateHolder<LoanItem> {
        return copy(data = emptyList(), pageNumber = 0)
    }

    companion object {
        val EMPTY = LoanListPaginationState(
            paginationState = PaginationState.Idle,
            data = emptyList(),
            pageNumber = 0,
            pageSize = LoanListViewModel.PAGE_SIZE,
            isUserBlocked = false,
        )
    }
}