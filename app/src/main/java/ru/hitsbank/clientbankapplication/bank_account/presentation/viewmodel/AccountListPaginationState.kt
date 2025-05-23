package ru.hitsbank.clientbankapplication.bank_account.presentation.viewmodel

import ru.hitsbank.bank_common.Constants.DEFAULT_PAGE_SIZE
import ru.hitsbank.bank_common.domain.entity.CurrencyCode
import ru.hitsbank.bank_common.presentation.pagination.PaginationState
import ru.hitsbank.bank_common.presentation.pagination.PaginationStateHolder
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.AccountItem
import ru.hitsbank.clientbankapplication.bank_account.presentation.model.CurrencyCodeDropdownItem

enum class AccountListMode(val topBarTitle: String) {
    DEFAULT("Счета"),
    HIDDEN_ACCOUNTS("Скрытые счета"),
    SELECTION("Выбор счета"),
}

data class AccountListPaginationState(
    override val paginationState: PaginationState,
    override val data: List<AccountItem>,
    override val pageNumber: Int,
    override val pageSize: Int,
    val isUserBlocked: Boolean,
    val createAccountDialogState: CreateAccountDialogState,
    val isPerformingAction: Boolean,
    val accountListMode: AccountListMode,
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
        fun default(accountListMode: AccountListMode) = AccountListPaginationState(
            paginationState = PaginationState.Idle,
            data = emptyList(),
            pageNumber = 1,
            pageSize = DEFAULT_PAGE_SIZE,
            isUserBlocked = false,
            createAccountDialogState = CreateAccountDialogState.Hidden,
            isPerformingAction = false,
            accountListMode = accountListMode,
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